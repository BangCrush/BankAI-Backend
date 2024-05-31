package com.hana.bankai.domain.product.controller;

import com.hana.bankai.domain.account.dto.AccountRequestDto;
import com.hana.bankai.domain.account.dto.AccountResponseDto;
import com.hana.bankai.domain.product.dto.ProductResponseDto;
import com.hana.bankai.domain.product.entity.ProdType;
import com.hana.bankai.domain.product.entity.Product;
import com.hana.bankai.domain.product.service.ProductService;
import com.hana.bankai.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.hana.bankai.global.common.response.SuccessCode.PRODUCT_SEARCH_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
@Slf4j
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "전체 상품 조회")
    @GetMapping("/")
    public ApiResponse<Map<ProdType,List<Product>>> allProd() {
        return productService.productSearchAll();
    }

    @Operation(summary = "상품 타입 별 조회")
    @GetMapping("/{prodtype}")
    public ApiResponse<List<ProductResponseDto.GetProduct>> searchDepositProd(@PathVariable("prodtype") int prodType) {

        return productService.getProduct(prodType);
    }
    @Operation(summary = "상품 타입 별 조회")
    @GetMapping("/detail")
    public ApiResponse<ProductResponseDto.GetProductDetail> detailProd(@RequestParam("code") Long productCode) {
        return productService.getProductDetail(productCode);
    }
    @Operation(summary = "상품 TOP 3 조회")
    @GetMapping("/top-three")
    public ApiResponse<List<ProductResponseDto.GetTopThree>> getProdTopThree() {
        return productService.prodTopThree();
    }
}
