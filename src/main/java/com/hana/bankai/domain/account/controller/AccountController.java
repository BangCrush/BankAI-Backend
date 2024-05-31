package com.hana.bankai.domain.account.controller;

import com.hana.bankai.domain.account.dto.AccountRequestDto;
import com.hana.bankai.domain.account.dto.AccountResponseDto;
import com.hana.bankai.domain.account.service.AccountService;
import com.hana.bankai.domain.accounthistory.service.AccHisService;
import com.hana.bankai.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
@Slf4j
public class AccountController {

    private final AccountService accountService;
    private final AccHisService accHisService;

    @Operation(summary = "계좌 잔액 조회")
    @GetMapping("/balance")
    public ApiResponse<AccountResponseDto.GetBalance> getBalance(@RequestBody AccountRequestDto.AccCodeReq request, @AuthenticationPrincipal UserDetails user) {
        return accountService.getBalance(request, user.getUsername());
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
    public ApiResponse transfer(@RequestBody AccountRequestDto.Transfer request, @AuthenticationPrincipal UserDetails user) {
        return accountService.transfer(request, user.getUsername());
    }

    @Operation(summary = "거래내역 조회")
    @GetMapping("/history")
    public ApiResponse<List<AccountResponseDto.GetAccHis>> getAccHis(@RequestBody AccountRequestDto.AccCodeReq request, @RequestParam("page") int page, @AuthenticationPrincipal UserDetails user) {
        return accHisService.getAccHis(request, user.getUsername(), page);
    }

    @Operation(summary = "사용자 보유 계좌 조회")
    @GetMapping("/list")
    public ApiResponse<List<AccountResponseDto.GetAccInfo>> getAccList(@AuthenticationPrincipal UserDetails user) {
        return accountService.getAccList(user.getUsername());
    }

    @Operation(summary = "사용자 총 자산 조회")
    @GetMapping("/assets")
    public ApiResponse<AccountResponseDto.GetAssets> getAssests(@AuthenticationPrincipal UserDetails user) {
        return accountService.getAssets(user.getUsername());
    }

    @Operation(summary = "계좌 생성")
    @PostMapping("/opening")
    public ApiResponse<AccountResponseDto.JoinAcc> openAcc(@RequestBody AccountRequestDto.ProdJoinReq request) {
        return accountService.joinAcc(request);
    }

    @Operation(summary = "계좌 해지")
    @DeleteMapping("/closing")
    public ApiResponse<Object> closeAcc(@RequestBody AccountRequestDto.CheckAccPwd request) {
        return accountService.terminationAcc(request);
    }

    @Operation(summary = "계좌 이체 한도 수정")
    @PutMapping("/update-limit")
    public ApiResponse<Object> modifyTrsfLimitAcc(@RequestBody AccountRequestDto.TrsfLimitModify request,
                                                  @AuthenticationPrincipal UserDetails user) {
        return accountService.accTrsfLimitModify(request, user.getUsername());
    }

}