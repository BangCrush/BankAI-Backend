package com.hana.bankai.domain.user.controller;

import com.hana.bankai.domain.user.dto.UserRequestDto;
import com.hana.bankai.domain.user.dto.UserResponseDto;
import com.hana.bankai.domain.user.service.MailService;
import com.hana.bankai.domain.user.service.UserService;
import com.hana.bankai.global.common.response.ApiResponse;
import com.hana.bankai.global.error.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.hana.bankai.global.error.ErrorCode.USER_REGISTER_VALIDATION_FAIL;
import static com.hana.bankai.global.error.ErrorCode.USER_REISSUE_FAIL;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MailService mailService;

    /* register */

    @Operation(summary = "회원가입 여부 확인")
    @PostMapping("/register/check")
    public ApiResponse<UserResponseDto.RegisterDuplicateCheck> registerCheck(@RequestBody UserRequestDto.RegisterCheck request) {
        return userService.registerCheck(request);
    }

    @Operation(summary = "이메일 중복 여부 확인")
    @PostMapping("/register/check-email")
    public ApiResponse<UserResponseDto.RegisterDuplicateCheck> registerCheckEmail(@RequestBody UserRequestDto.RegisterCheckEmail request) {
        return userService.registerCheckEmail(request);
    }

    @Operation(summary = "아이디 중복 여부 확인")
    @PostMapping("/register/check-id")
    public ApiResponse<UserResponseDto.RegisterDuplicateCheck> registerCheckId(@RequestBody UserRequestDto.RegisterCheckId request) {
        return userService.registerCheckId(request);
    }

    @Operation(summary = "회원가입")
    @PostMapping("/register")
    public ApiResponse<Object> register(@RequestBody @Validated UserRequestDto.Register request, Errors errors) {
        // validation check
        if(errors.hasErrors()) {
            throw new CustomException(USER_REGISTER_VALIDATION_FAIL);
        }

        return userService.register(request);
    }

    /* login */

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ApiResponse<UserResponseDto.TokenInfo> login(@RequestBody UserRequestDto.Login request) {
        return userService.login(request);
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/login/reissue")
    public ApiResponse<UserResponseDto.TokenInfo> reissue(@RequestBody UserRequestDto.Reissue reissue, Errors errors) {
        // validation check -> ## 어떤 경우에 예외? 강사님께 여쭤보기 ##
        if(errors.hasErrors()) {
            throw new CustomException(USER_REISSUE_FAIL);
        }

        return userService.reissue(reissue);
    }

    @Operation(summary = "아이디 찾기")
    @PostMapping("/login/find-id")
    public ApiResponse<UserResponseDto.LoginFindId> loginFindId(@RequestBody UserRequestDto.LoginFindId request) {
        return userService.loginFindId(request);
    }

    @Operation(summary = "임시 비밀번호 발급")
    @PostMapping("/login/temp-pwd")
    public ApiResponse<UserResponseDto.LoginTempPwd> tempPassword(@RequestBody UserRequestDto.LoginTempPwd request) {
        return mailService.tempPasswordEmail(request);
    }

    /* logout */

    @Operation(summary = "로그아웃")
    @PostMapping("/signout")
    public ApiResponse<Object> signout(@RequestBody UserRequestDto.Logout request) {
        return userService.logout(request);
    }

    /* users */

    @Operation(summary = "회원 정보 조회")
    @GetMapping("/users/user-info")
    public ApiResponse<UserResponseDto.UserInfo> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.getUserInfo(userDetails.getUsername());
    }

    @Operation(summary = "회원 정보 수정")
    @PutMapping("/users/user-info")
    public ApiResponse<Object> updateUserInfo(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UserRequestDto.UserInfo userInfo) {
        return userService.updateUserInfo(userDetails.getUsername(), userInfo);
    }

    @Operation(summary = "직업 정보 조회")
    @GetMapping("/users/job-info")
    public ApiResponse<UserResponseDto.UserJobInfo> getUserJobInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.getUserJobInfo(userDetails.getUsername());
    }

    @Operation(summary = "직업 정보 수정")
    @PutMapping("/users/job-info")
    public ApiResponse<Object> updateUserJobInfo(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UserRequestDto.UserJobInfo userJobInfo) {
        return userService.updateUserJobInfo(userDetails.getUsername(), userJobInfo);
    }

    @Operation(summary = "이메일 인증")
    @PostMapping("/register/authenticate")
    public ApiResponse<String> AuthenticateEmail(@RequestParam("email") String email) {
        return mailService.authenticateEmail(email);
    }

    @Operation(summary = "휴대폰 인증번호 발송")
    @PostMapping("/register/sms-certification/send")
    public ApiResponse sendSms(@RequestBody UserRequestDto.SmsCertificate request) {
        return userService.sendSms(request.getUserPhone());
    }

    @Operation(summary = "휴대폰 인증번호 확인")
    @PostMapping("/register/sms-certification/verify")
    public ApiResponse verifySms(@RequestBody UserRequestDto.SmsVerify request) {
        return userService.verifySms(request);
    }
}
