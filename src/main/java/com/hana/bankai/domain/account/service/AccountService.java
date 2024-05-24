package com.hana.bankai.domain.account.service;

import com.hana.bankai.domain.account.dto.AccountRequestDto;
import com.hana.bankai.domain.account.dto.AccountResponseDto;
import com.hana.bankai.domain.account.entity.AccStatus;
import com.hana.bankai.domain.account.entity.Account;
import com.hana.bankai.domain.account.repository.AccountRepository;
import com.hana.bankai.global.common.response.ApiResponse;
import com.hana.bankai.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hana.bankai.global.common.response.SuccessCode.*;
import static com.hana.bankai.global.error.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    final private AccountRepository accountRepository;

    public ApiResponse<AccountResponseDto.GetBalance> getBalance(AccountRequestDto.AccCodeReq request) {
        // 사용자 인증 확인 (개발 예정)

        Long balance = accountRepository.findAccBalanceByAccCode(request.getAccCode())
                .orElseThrow(() -> new CustomException(ACCOUNT_NOT_FOUND));

        return ApiResponse.success(ACCOUNT_BALANCE_CHECK_SUCCESS, new AccountResponseDto.GetBalance(balance));
    }

    public ApiResponse<AccountResponseDto.SearchAcc> searchAcc(AccountRequestDto.AccCodeReq request) {
        Account account = accountRepository.findById(request.getAccCode())
                .orElseThrow(() -> new CustomException(ACCOUNT_NOT_FOUND));

        // 해지된 계좌인지 확인
        if (account.getStatus() == AccStatus.DELETED) {
            throw new CustomException(ACCOUNT_NOT_FOUND);
        }

        String userName = account.getUser().getUserName();
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
}

