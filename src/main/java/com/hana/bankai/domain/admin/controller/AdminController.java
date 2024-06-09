package com.hana.bankai.domain.admin.controller;

import com.hana.bankai.domain.admin.dto.AdminResponseDto;
import com.hana.bankai.domain.admin.service.AdminService;
import com.hana.bankai.domain.user.dto.UserRequestDto;
import com.hana.bankai.domain.user.dto.UserResponseDto;
import com.hana.bankai.domain.user.entity.Role;
import com.hana.bankai.domain.user.service.UserService;
import com.hana.bankai.global.common.response.ApiResponse;
import com.hana.bankai.domain.product.entity.ProdType;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ApiResponse<UserResponseDto.TokenInfo> login(@RequestBody UserRequestDto.Login request) {
        return userService.login(request, Role.ROLE_ADMIN);
    }

    @Operation(summary = "라인 차트")
    @GetMapping("/line-chart")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public Map<String, Map<Integer, Integer>> getLineChartData(@RequestParam("year") int year) {
        return adminService.getLineChartData(year);
    }

    @Operation(summary = "막대 차트")
    @GetMapping("/bar-chart")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public Map<ProdType, List<AdminResponseDto.ProductJoinCntByAgeGroup>> getBarChartData() {
        return adminService.getBarChartData();
    }

    @Operation(summary = "파이 차트")
    @GetMapping("/pie-chart")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ApiResponse<List<Integer>> getProdJoinRate() {
        return adminService.getProdJoinRate();
    }


}
