package com.hana.bankai.domain.admin.controller;

import com.hana.bankai.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/line-chart")
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public Map<String, Map<Integer, Integer>> getBalance(@RequestParam("year") int year) {
        return adminService.getLineChartData(year);
    }

}
