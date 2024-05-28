package com.hana.bankai.domain.user.controller;

import com.hana.bankai.domain.account.dto.AccountRequestDto;
import com.hana.bankai.domain.account.dto.AccountResponseDto;
import com.hana.bankai.domain.user.dto.RefreshToken;
import com.hana.bankai.domain.user.dto.UserRequestDto;
import com.hana.bankai.domain.user.dto.UserResponseDto;
import com.hana.bankai.domain.user.service.TokenService;
import com.hana.bankai.domain.user.service.UserService;
import com.hana.bankai.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
//@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final TokenService tokenService;
    private final UserService userService;

    @PostMapping
    public RefreshToken createUser(@RequestBody RefreshToken user) {
        return tokenService.save(user);
    }

    @GetMapping("/{id}")
    public Optional<RefreshToken> getUser(@PathVariable String id) {
        return tokenService.findById(id);
    }

    @Operation(summary = "회원가입 여부 확인")
    @PostMapping("/register/check")
    public ApiResponse<UserResponseDto.RegisterCheck> registerCheck(@RequestBody UserRequestDto.RegisterCheck request) {
        return userService.registerCheck(request);
    }

}
