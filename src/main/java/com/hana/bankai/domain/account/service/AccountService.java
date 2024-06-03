package com.hana.bankai.domain.account.service;

import com.hana.bankai.domain.account.dto.AccountRequestDto;
import com.hana.bankai.domain.account.dto.AccountResponseDto;
import com.hana.bankai.domain.account.entity.AccCodeGenerator;
import com.hana.bankai.domain.account.entity.Account;
import com.hana.bankai.domain.account.repository.AccountRepository;
import com.hana.bankai.domain.accounthistory.entity.HisType;
import com.hana.bankai.domain.accounthistory.service.AccHisService;
import com.hana.bankai.domain.autotransfer.entity.AutoTransfer;
import com.hana.bankai.domain.autotransfer.entity.AutoTransferId;
import com.hana.bankai.domain.autotransfer.repository.AutoTransferRepository;
import com.hana.bankai.domain.product.entity.ProdType;
import com.hana.bankai.domain.product.repsoitory.ProductRepository;
import com.hana.bankai.domain.user.entity.User;
import com.hana.bankai.domain.user.repository.UserRepository;
import com.hana.bankai.domain.user.service.UserTrsfLimitService;
import com.hana.bankai.global.aop.DistributedLock;
import com.hana.bankai.domain.product.entity.Product;
import com.hana.bankai.global.common.response.ApiResponse;
import com.hana.bankai.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.hana.bankai.domain.account.entity.AccStatus.ACTIVE;
import static com.hana.bankai.domain.account.entity.AccStatus.DELETED;
import static com.hana.bankai.domain.accounthistory.entity.HisType.*;
import static com.hana.bankai.global.common.enumtype.BankCode.C04;
import static com.hana.bankai.global.common.response.SuccessCode.*;
import static com.hana.bankai.global.error.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccHisService accHisService;
    private final UserTrsfLimitService trsfLimitService;
    final PlatformTransactionManager transactionManager;
    private final AccCodeGenerator accCodeGenerator;
    private final ProductRepository productRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AutoTransferRepository autoTransferRepository;

    public ApiResponse<AccountResponseDto.GetBalance> getBalance(String accCode, String userId) {
        // 사용자 인증
        User user = getUserByUserId(userId);
        Account account = getAccByAccCode(accCode);
        authenticateUser(user, account.getUser());

        Long balance = retrieveBalance(accCode);

        return ApiResponse.success(ACCOUNT_BALANCE_CHECK_SUCCESS, new AccountResponseDto.GetBalance(balance));
    }

    public ApiResponse<AccountResponseDto.SearchAcc> searchAcc(String accCode) {
        Account account = getAccByAccCode(accCode);

        // 해지된 계좌인지 확인
        checkAccStatus(account);
        String userName = account.getUser().getUserNameKr();
        return ApiResponse.success(ACCOUNT_SEARCH_SUCCESS, new AccountResponseDto.SearchAcc(accCode, userName));
    }

    public ApiResponse<AccountResponseDto.CheckRes> checkTransferLimit(AccountRequestDto.CheckTransferLimit request) {
        Long balance = accountRepository.findAccBalanceByAccCode(request.getAccCode())
                .orElseThrow(() -> new CustomException(ACCOUNT_NOT_FOUND));
        boolean isTransferAble = balance >= request.getAmount();
        return ApiResponse.success(ACCOUNT_LIMIT_CHECK_SUCCESS, new AccountResponseDto.CheckRes(isTransferAble));
    }

    public ApiResponse<AccountResponseDto.CheckRes> checkAccPw(AccountRequestDto.CheckAccPwd request) {
        boolean isPwdValid = checkAccountByAccPwd(request.getAccCode(), request.getAccPwd());
        return ApiResponse.success(ACCOUNT_PWD_CHECK_SUCCESS, new AccountResponseDto.CheckRes(isPwdValid));
    }

    @DistributedLock(key = "#request.getOutAccCode()")
    public ApiResponse transfer(AccountRequestDto.Transfer request, String userId, HisType hisType) {
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
            accHisService.createAccHis(request.getAmount(), hisType, inAcc, outAcc);
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

        return ApiResponse.success(USER_ASSETS_CHECK_SUCCESS, new AccountResponseDto.GetAssets(user.getUserNameKr(), assets));
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

    // 상품가입 (계좌개설)
    public ApiResponse<AccountResponseDto.JoinAcc> joinAcc(AccountRequestDto.ProdJoinReq request, String userId) {
        // User 객체 조회
        User userEntity = getUserByUserId(userId);

        // Product 객체 불러오기
        Product productEntity = productRepository.findById(request.getProdCode())
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_SEARCH));

        // 만기일 설정을 위한 시간 조회
        LocalDate now = LocalDate.now();

        // 계좌 생성 및 계좌번호 중복 체크
        Account savedAccount;
        String accCode;
        do {
            accCode = accCodeGenerator.generateAccCode();
        } while (accountRepository.existsByAccCode(accCode));

        Account.AccountBuilder accountBuilder = Account.builder()
                .accCode(accCode)
                .product(productEntity)
                .user(userEntity)
                .accTime(now.plusMonths(request.getPeriod())) // plusMonths() 으로 만기일 지정
                .accPwd(passwordEncoder.encode(request.getAccountPwd()))
                .status(ACTIVE);

        if (request.getAmount() != null)
            accountBuilder.accBalance(request.getAmount());

        if (request.getAccTrsfLimit() != null)
            accountBuilder.accTrsfLimit(request.getAccTrsfLimit());

        savedAccount = accountBuilder.build();

        accountRepository.save(savedAccount);

        //이체
        String prodType = String.valueOf(productEntity.getProdType());
        if (prodType.equals("SAVINGS") || prodType.equals("LOAN")  ){
            // 자동 이체 설정 (적금 또는 대출일 때만)
            setAutoTransfer(request, productEntity, savedAccount);
        }
        if (prodType.equals("DEPOSIT")){
            // 예금 상품일경우 주거래은행에서 돈 출금
            setDepositTransfer(accCode, userEntity, request, userId);
        }
        AccountResponseDto.JoinAcc code = new AccountResponseDto.JoinAcc(savedAccount.getAccCode());
        return ApiResponse.success(ACCOUNT_CREATE_SUCCESS, code);
    }

    // 계좌해지
    public ApiResponse<Object> terminationAcc(AccountRequestDto.CheckAccPwd request) {
        // 계좌 조회
        Account account = accountRepository.findByAccCode(request.getAccCode())
                .orElseThrow(() -> new CustomException(ACCOUNT_NOT_FOUND));

        // 비밀번호 확인
        if (checkAccountByAccPwd(request.getAccCode(), request.getAccPwd())) {
            throw new CustomException(ACCOUNT_PWD_FAIL);
        };

        account.setStatus(DELETED);
        account.setAccBalance(0L);

        return ApiResponse.success(ACCOUNT_DELETE_SUCCESS);
    }

    // 이체한도 수정(각 계좌별)
    public ApiResponse<Object> accTrsfLimitModify(AccountRequestDto.TrsfLimitModify request, String userId) {
        // 회원 이체한도 보다 많을 수 없음 체크
        User checkUser = getUserByUserId(userId);
        Long limit = userRepository.getUserLimit(checkUser.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (request.getAccTrsfLimit() > limit) {
            throw new CustomException(LIMIT_MODIFY_FAIL);
        }

        // 비밀번호 확인
        if (checkAccountByAccPwd(request.getAccCode(), request.getAccPwd())) {
            throw new CustomException(ACCOUNT_PWD_FAIL);
        };

        // 이체한도 수정
        Account account = accountRepository.findByAccCode(request.getAccCode())
                .orElseThrow(() -> new CustomException(ACCOUNT_NOT_FOUND));

        account.setAccTrsfLimit(request.getAccTrsfLimit());
        return ApiResponse.success(ACCOUNT_LIMIT_MODIFY_SUCCESS);
    }

    // 자동 이체 설정
    private void setAutoTransfer(AccountRequestDto.ProdJoinReq request, Product product, Account savedAccount) {
        // 적금 또는 대출일 때만 실행
        if(product.getProdType().equals(ProdType.SAVINGS) || product.getProdType().equals(ProdType.LOAN)) {
            // 출금 계좌 조회
            Account outAccount = getAccByAccCode(request.getOutAccount());

            // 자동이체 Entity 생성
            AutoTransferId autoTransferId = AutoTransferId.builder()
                    .atDate(request.getAtDate())
                    .inAccCode(savedAccount.getAccCode())
                    .outAccCode(request.getOutAccount())
                    .build();

            AutoTransfer autoTransfer = AutoTransfer.builder()
                    .autoTransferId(autoTransferId)
                    .inBankCode(request.getInBankCode())
                    .atAmount(request.getAtAmount())
                    .account(outAccount)
                    .build();

            // 저장
            autoTransferRepository.save(autoTransfer);
        }
    }

    private void setDepositTransfer(String accCode, User userEntity,
                                    AccountRequestDto.ProdJoinReq request, String userId) {
        AccountRequestDto.Transfer transferAccount = AccountRequestDto.Transfer.builder()
                .inAccCode(accCode)
                .inBankCode(C04)
                .outAccCode(userEntity.getUserMainAcc())
                .outBankCode(C04)
                .amount(request.getAmount())
                .build();

        transfer(transferAccount, userId, TRANSFER);
    }

   // 중복 함수 분리
    private Boolean checkAccountByAccPwd(String accCode, String accPwd) {
        String accPwdCheck = accountRepository.findAccPwdByAccCode(accCode)
                .orElseThrow(() -> new CustomException(ACCOUNT_NOT_FOUND));

        return passwordEncoder.matches(accPwd, accPwdCheck);
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

    private void checkAccStatus(Account acc) {
        if (acc.getStatus() == DELETED) {
            throw new CustomException(ACCOUNT_NOT_FOUND);
        }
    }

    private Account getAccByAccCode(String accCode) {
        return accountRepository.findById(accCode)
                .orElseThrow(() -> new CustomException(ACCOUNT_NOT_FOUND));
    }

    private Long retrieveBalance(String accCode) {
        return accountRepository.findAccBalanceByAccCode(accCode)
                .orElseThrow(() -> new CustomException(ACCOUNT_NOT_FOUND));
    }

}

