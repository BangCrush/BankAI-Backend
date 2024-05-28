package com.hana.bankai.domain.account.controller;

import com.hana.bankai.domain.account.dto.AccountRequestDto;
import com.hana.bankai.domain.account.dto.AccountResponseDto;
import com.hana.bankai.domain.account.service.AccountService;
import com.hana.bankai.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    final private AccountService accountService;

    @Operation(summary = "계좌 잔액 조회")
    @GetMapping("/balance")
    public ApiResponse<AccountResponseDto.GetBalance> getBalance(@RequestBody AccountRequestDto.AccCodeReq request) {
        return accountService.getBalance(request);
    }

    @Operation(summary = "계좌 검색")
    @GetMapping("/search")
    public ApiResponse<AccountResponseDto.SearchAcc> searchAcc(@RequestBody AccountRequestDto.AccCodeReq request) {
        return accountService.searchAcc(request);
    }

    @Operation(summary = "이체한도 체크")
    @PostMapping("/check-limit")
    public ApiResponse<AccountResponseDto.CheckRes> checkTransferLimit(@RequestBody AccountRequestDto.CheckTransferLimit request) {
        return accountService.checkTransferLimit(request);
    }

    @Operation(summary = "계좌 비밀번호 체크")
    @PostMapping("/check-pw")
    public ApiResponse<AccountResponseDto.CheckRes> checkAccPwd(@RequestBody AccountRequestDto.CheckAccPwd request) {
        return accountService.checkAccPw(request);
    }

    @Operation(summary = "계좌이체")
    @PostMapping("/transfer")
    public ApiResponse transfer(@RequestBody AccountRequestDto.Transfer request) {
        return accountService.transfer(request);
    }
}