package com.hana.bankai.domain.account.service;

import com.hana.bankai.domain.account.dto.AccountRequestDto;
import com.hana.bankai.domain.account.dto.AccountResponseDto;
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

    public ApiResponse<AccountResponseDto.GetBalance> getBalance(AccountRequestDto.GetBalance request) {
        // 사용자 인증 확인 (개발 예정)

        Long balance = accountRepository.findAccBalanceByAccCode(request.getAccCode())
                .orElseThrow(() -> new CustomException(ACCOUNT_NOT_FOUND));

        return ApiResponse.success(ACCOUNT_BALANCE_CHECK_SUCCESS, new AccountResponseDto.GetBalance(balance));
    }
}

