package com.hana.bankai.domain.user.controller;

import com.hana.bankai.domain.user.dto.UserRequestDto;
import com.hana.bankai.domain.user.dto.UserResponseDto;
import com.hana.bankai.domain.user.service.UserService;
import com.hana.bankai.global.common.response.ApiResponse;
import com.hana.bankai.global.error.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.hana.bankai.global.error.ErrorCode.USER_REGISTER_VALIDATION_FAIL;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
    public ApiResponse<Object> register(@Validated @RequestBody UserRequestDto.Register request, Errors errors) {
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

    @Operation(summary = "아이디 찾기")
    @PostMapping("/login/find-id")
    public ApiResponse<UserResponseDto.LoginFindId> loginFindId(@RequestBody UserRequestDto.LoginFindId request) {
        return userService.loginFindId(request);
    }

    @Operation(summary = "비밀번호 찾기")
    @PostMapping("/login/find-pwd")
    public ApiResponse<UserResponseDto.LoginFindPwd> loginFindId(@RequestBody UserRequestDto.LoginFindPwd request) {
        return userService.loginFindPwd(request);
    }

    /* logout */

    @Operation(summary = "로그아웃")
    @PostMapping("/logouts")
    public ApiResponse<Object> logout(@RequestBody UserRequestDto.Logout request) {
        return userService.logout(request);
    }

}
