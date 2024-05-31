package com.hana.bankai.domain.account.service;

import com.hana.bankai.domain.account.dto.AccountRequestDto;
import com.hana.bankai.domain.account.dto.AccountResponseDto;
import com.hana.bankai.domain.account.entity.AccStatus;
import com.hana.bankai.domain.account.entity.Account;
import com.hana.bankai.domain.account.repository.AccountRepository;
import com.hana.bankai.domain.accounthistory.service.AccHisService;
import com.hana.bankai.domain.user.entity.User;
import com.hana.bankai.domain.user.repository.UserRepository;
import com.hana.bankai.domain.user.service.UserTrsfLimitService;
import com.hana.bankai.global.aop.DistributedLock;
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

import static com.hana.bankai.domain.accounthistory.entity.HisType.*;
import static com.hana.bankai.global.common.response.SuccessCode.*;
import static com.hana.bankai.global.error.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccHisService accHisService;
    private final UserTrsfLimitService trsfLimitService;
    final PlatformTransactionManager transactionManager;

    public ApiResponse<AccountResponseDto.GetBalance> getBalance(AccountRequestDto.AccCodeReq request, String userId) {
        // 사용자 인증
        User user = getUserByUserId(userId);
        Account account = getAccByAccCode(request.getAccCode());
        authenticateUser(user, account.getUser());

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
    public ApiResponse transfer(AccountRequestDto.Transfer request, String userId) {
        User user = getUserByUserId(userId);

        Account outAcc = getAccByAccCode(request.getOutAccCode());
        Account inAcc = getAccByAccCode(request.getInAccCode());

        // 사용자 인증 예외처리
        authenticateUser(user, outAcc.getUser());
        // 해지된 계좌 예외처리
        checkAccStatus(inAcc);
        // 유효하지 않은 이체 금액 예외처리
        if (request.getAmount() < 0) {
            throw new CustomException(INVALID_TRANSFER_AMOUNT);
        }
        // 이체한도 예외처리
        if (outAcc.getAccTrsfLimit() < request.getAmount() || trsfLimitService.checkTrsfLimit(user.getUserCode(), request.getAmount())) {
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
            trsfLimitService.accumulate(user.getUserCode(), request.getAmount());
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

    public ApiResponse<List<AccountResponseDto.GetAccInfo>> getAccList(String userId) {
        List<AccountResponseDto.GetAccInfo> accInfoList = new ArrayList<>();

        User user = getUserByUserId(userId);
        for (Account acc : user.getAccountList()) {
            accInfoList.add(AccountResponseDto.GetAccInfo.from(acc));
        }

        return ApiResponse.success(ACCOUNT_LIST_CHECK_SUCCESS, accInfoList);
    }

    public ApiResponse<AccountResponseDto.GetAssets> getAssets(String userId) {
        User user = getUserByUserId(userId);

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

    private User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    private void authenticateUser(User loginUser, User accUser) {
        if (!loginUser.equals(accUser)) {
            throw new CustomException(USER_AUTHENTICATION_FAIL);
        }
    }
}

