package com.hana.bankai.domain.admin.controller;

import com.hana.bankai.domain.admin.dto.AdminResponseDto;
import com.hana.bankai.domain.admin.service.AdminService;
import com.hana.bankai.global.common.response.ApiResponse;
import com.hana.bankai.domain.product.entity.ProdType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/line-chart")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public Map<String, Map<Integer, Integer>> getLineChartData(@RequestParam("year") int year) {
        return adminService.getLineChartData(year);
    }

    @GetMapping("/bar-chart")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public Map<ProdType, List<AdminResponseDto.ProductJoinCntByAgeGroup>> getBarChartData() {
        return adminService.getBarChartData();
    }

    @GetMapping("/pie-chart")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ApiResponse<List<Integer>> getProdJoinRate() {
        return adminService.getProdJoinRate();
    }


}
