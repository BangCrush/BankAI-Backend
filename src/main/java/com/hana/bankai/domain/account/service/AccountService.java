package com.hana.bankai.domain.account.service;

import com.hana.bankai.domain.account.dto.AccountRequestDto;
import com.hana.bankai.domain.account.dto.AccountResponseDto;
import com.hana.bankai.domain.account.entity.AccStatus;
import com.hana.bankai.domain.account.entity.Account;
import com.hana.bankai.domain.account.repository.AccountRepository;
import com.hana.bankai.domain.accounthistory.entity.AccountHistory;
import com.hana.bankai.domain.accounthistory.repository.AccHisRepository;
import com.hana.bankai.domain.accounthistory.service.AccHisService;
import com.hana.bankai.domain.user.entity.User;
import com.hana.bankai.domain.user.repository.UserRepository;
import com.hana.bankai.domain.user.service.UserTrsfLimitService;
import com.hana.bankai.global.aop.DistributedLock;
import com.hana.bankai.global.common.enumtype.BankCode;
import com.hana.bankai.global.common.response.ApiResponse;
import com.hana.bankai.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.hana.bankai.domain.accounthistory.entity.HisType.*;
import static com.hana.bankai.global.common.response.SuccessCode.*;
import static com.hana.bankai.global.error.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    final private AccountRepository accountRepository;
    final private AccHisRepository accHisRepository;
    final private UserRepository userRepository;
    final private AccHisService accHisService;
    final private UserTrsfLimitService trsfLimitService;
    final PlatformTransactionManager transactionManager;

    public ApiResponse<AccountResponseDto.GetBalance> getBalance(AccountRequestDto.AccCodeReq request) {
        // 사용자 인증 확인 (개발 예정)

        Long balance = retrieveBalance(request.getAccCode());

        return ApiResponse.success(ACCOUNT_BALANCE_CHECK_SUCCESS, new AccountResponseDto.GetBalance(balance));
    }

    public ApiResponse<AccountResponseDto.SearchAcc> searchAcc(AccountRequestDto.AccCodeReq request) {
        Account account = getAccByAccCode(request.getAccCode());

        // 해지된 계좌인지 확인
        checkAccStatus(account);

        String userName = account.getUser().getUserNameKr();
        return ApiResponse.success(ACCOUNT_SEARCH_SUCCESS, new AccountResponseDto.SearchAcc(request.getAccCode(), userName));
    }

    public ApiResponse<AccountResponseDto.CheckRes> checkTransferLimit(AccountRequestDto.CheckTransferLimit request) {
        Long balance = accountRepository.findAccBalanceByAccCode(request.getAccCode())
                .orElseThrow(() -> new CustomException(ACCOUNT_NOT_FOUND));

        boolean isTransferAble = balance >= request.getAmount();

        return ApiResponse.success(ACCOUNT_LIMIT_CHECK_SUCCESS, new AccountResponseDto.CheckRes(isTransferAble));
    }

    public ApiResponse<AccountResponseDto.CheckRes> checkAccPw(AccountRequestDto.CheckAccPwd request) {
        String accPwd = accountRepository.findAccPwdByAccCode(request.getAccCode())
                .orElseThrow(() -> new CustomException(ACCOUNT_NOT_FOUND));

        boolean isPwdValid = accPwd.equals(request.getAccPwd());
        return ApiResponse.success(ACCOUNT_PWD_CHECK_SUCCESS, new AccountResponseDto.CheckRes(isPwdValid));
    }

    @DistributedLock(key = "#request.getOutAccCode()")
    public ApiResponse transfer(AccountRequestDto.Transfer request) {
        // 사용자 인증 확인 (개발 예정)
        // 테스트용
        String uuidString = "cff1daa8-41f5-49ef-9150-5a6106525c57";
        UUID userCode = UUID.fromString(uuidString);

        Account outAcc = getAccByAccCode(request.getOutAccCode());
        Account inAcc = getAccByAccCode(request.getInAccCode());

        // 해지된 계좌 예외처리
        checkAccStatus(inAcc);
        // 유효하지 않은 이체 금액 예외처리
        if (request.getAmount() < 0) {
            throw new CustomException(INVALID_TRANSFER_AMOUNT);
        }
        // 이체한도 예외처리
        if (outAcc.getAccTrsfLimit() < request.getAmount() || trsfLimitService.checkTrsfLimit(userCode, request.getAmount())) {
            throw new CustomException(TRANSFER_LIMIT_EXCEEDED);
        }

        // 트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            bizLogic(outAcc, inAcc, request.getAmount());
            transactionManager.commit(status); // 성공 시 커밋
            // 계좌 로그 생성
            accHisService.createAccHis(request.getAmount(), TRANSFER, inAcc, outAcc);
            // 사용자 이체 한도 > 누적 금액 수정
            trsfLimitService.accumulate(userCode, request.getAmount());
        } catch (CustomException e) {
            if (e.getErrorCode().getCode().equals("E201")) {
                throw e;
            }
        } catch (Exception e) {
            transactionManager.rollback(status); // 실패 시 롤백
            throw new CustomException(ACCOUNT_TRANSFER_FAIL);
        }

        return ApiResponse.success(ACCOUNT_TRANSFER_SUCCESS);
    }

    public ApiResponse<List<AccountResponseDto.GetAccHis>> getAccHis(AccountRequestDto.AccCodeReq request) {
        // 예외처리
        Account acc = getAccByAccCode(request.getAccCode());

        List<AccountResponseDto.GetAccHis> accHisList = new ArrayList<>();
        try {
            // 입금
            List<AccountHistory> accHisDepositList = accHisRepository.findByInAccCodeAndInBankCode(acc.getAccCode(), BankCode.C04);
            // 출금
            List<AccountHistory> accHisWithdrawList = accHisRepository.findByOutAccCodeAndOutBankCode(acc.getAccCode(), BankCode.C04);

            accHisList.addAll(makeAccHisDataList(accHisDepositList, true));
            accHisList.addAll(makeAccHisDataList(accHisWithdrawList, false));
        } catch (Exception e) {
            throw new CustomException(FIND_ACCOUNT_HISTORY_FAIL);
        }

        return ApiResponse.success(ACCOUNT_HISTORY_CHECK_SUCCESS, accHisList);
    }

    public ApiResponse<List<AccountResponseDto.GetAccInfo>> getAccList() {
        // 로그인한 사용자 정보 불러오기 (개발 예정)
        // 테스트용
        String uuidString = "cff1daa8-41f5-49ef-9150-5a6106525c57";
        UUID userCode = UUID.fromString(uuidString);

        List<AccountResponseDto.GetAccInfo> accInfoList = new ArrayList<>();

        User user = getUserByUserCode(userCode);
        for (Account acc : user.getAccountList()) {
            accInfoList.add(AccountResponseDto.GetAccInfo.from(acc));
        }

        return ApiResponse.success(ACCOUNT_LIST_CHECK_SUCCESS, accInfoList);
    }

    public ApiResponse<AccountResponseDto.GetAssets> getAssets() {
        // 로그인한 사용자 정보 불러오기 (개발 예정)
        // 테스트용
        String uuidString = "cff1daa8-41f5-49ef-9150-5a6106525c57";
        UUID userCode = UUID.fromString(uuidString);
        User user = getUserByUserCode(userCode);

        Long assets = 0L;
        for (Account acc : user.getAccountList()) {
            assets += acc.getAccBalance();
        }

        return ApiResponse.success(USER_ASSETS_CHECK_SUCCESS, new AccountResponseDto.GetAssets(assets));
    }

    private Account getAccByAccCode(String accCode) {
        return accountRepository.findById(accCode)
                .orElseThrow(() -> new CustomException(ACCOUNT_NOT_FOUND));
    }

    private Long retrieveBalance(String accCode) {
        return accountRepository.findAccBalanceByAccCode(accCode)
                .orElseThrow(() -> new CustomException(ACCOUNT_NOT_FOUND));
    }

    private void bizLogic(Account outAcc, Account inAcc, Long money) {
        // 잔액 확인
        if (retrieveBalance(outAcc.getAccCode()) < money) {
            throw new CustomException(INSUFFICIENT_BALANCE);
        }
        // 출금
        outAcc.transfer(-money);
        // 입금
        inAcc.transfer(money);
    }

    private void checkAccStatus(Account acc) {
        if (acc.getStatus() == AccStatus.DELETED) {
            throw new CustomException(ACCOUNT_NOT_FOUND);
        }
    }

    private List<AccountResponseDto.GetAccHis> makeAccHisDataList(List<AccountHistory> accHisList, boolean isDeposit) {
        List<AccountResponseDto.GetAccHis> accHisDataList = new ArrayList<>();

        for (AccountHistory accHisReq : accHisList) {
            String targetAccCode = isDeposit ? accHisReq.getOutAccCode() : accHisReq.getInAccCode();
            Account targetAcc = getAccByAccCode(targetAccCode);
            User targetUser = getUserByUserCode(targetAcc.getUser().getUserCode());

            Long hisAmount = isDeposit ? accHisReq.getHisAmount() : -accHisReq.getHisAmount();
            Long balance = isDeposit ? accHisReq.getAfterInBal() : accHisReq.getAfterOutBal();

            accHisDataList.add(AccountResponseDto.GetAccHis.of(accHisReq, targetUser.getUserName(), hisAmount, balance));
        }

        return accHisDataList;
    }

    private User getUserByUserCode(UUID userCode) {
        return userRepository.findById(userCode)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }
}

