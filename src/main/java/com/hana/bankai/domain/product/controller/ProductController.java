package com.hana.bankai.domain.product.controller;

import com.hana.bankai.domain.account.dto.AccountRequestDto;
import com.hana.bankai.domain.account.dto.AccountResponseDto;
import com.hana.bankai.domain.product.dto.ProductResponseDto;
import com.hana.bankai.domain.product.entity.Product;
import com.hana.bankai.domain.product.service.ProductService;
import com.hana.bankai.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
@Slf4j
public class ProductController {
    private final ProductService productService;

//    @Operation(summary = "상품 조회")
//    @GetMapping("/")
//    public ApiResponse<ProductResponseDto.GetProduct> searchProd() {
//        return productService.getProduct();
//    }
    @Operation(summary = "상품 타입 별 조회")
    @GetMapping("/{prodtype}")
    public ApiResponse<List<ProductResponseDto.GetProduct>> searchDepositProd(@PathVariable("prodtype") String prodType) {
        log.info(">>>>>>>>>>>>"+prodType);
        return productService.getProduct(prodType);
    }
}
