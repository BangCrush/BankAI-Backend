package com.hana.bankai.domain.product.service;

import com.hana.bankai.domain.account.repository.AccountRepository;
import com.hana.bankai.domain.product.dto.ProductResponseDto;
import com.hana.bankai.domain.product.entity.ProdType;
import com.hana.bankai.domain.product.entity.Product;
import com.hana.bankai.domain.product.repsoitory.ProductRepository;
import com.hana.bankai.global.common.response.ApiResponse;
import com.hana.bankai.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.hana.bankai.global.common.response.SuccessCode.PRODUCT_SEARCH_SUCCESS;
import static com.hana.bankai.global.error.ErrorCode.PRODUCT_NOT_SEARCH;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;

    // 상품 디테일 페이지
    public ApiResponse<Product> getProductDetail(Long prodCode) {
        Product p = productRepository.findById(prodCode)
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_SEARCH));
        return ApiResponse.success(PRODUCT_SEARCH_SUCCESS, p);
    }

    // 상품 별 전체 조회 코드
    public ApiResponse<Map<ProdType, List<ProductResponseDto.GetProduct>>> getProduct(int prodType) {
        ProdType prodTypeNum = ProdType.of(prodType);

        List<Product> productList = productRepository.findByProdType(prodTypeNum);

        if (productList.isEmpty()) {
            throw new CustomException(PRODUCT_NOT_SEARCH);
        }

        List<ProductResponseDto.GetProduct> productDtoList = new ArrayList<>();

        for (Product p : productList) {
            ProductResponseDto.GetProduct prodDto = ProductResponseDto.GetProduct.from(p);
            productDtoList.add(prodDto);
        }

        Map<ProdType, List<ProductResponseDto.GetProduct>> prodTypeListMap = new HashMap<>();

        prodTypeListMap.put(prodTypeNum, productDtoList);

        return ApiResponse.success(PRODUCT_SEARCH_SUCCESS, prodTypeListMap); // Pass the product object
    }

    // 전체 조회 코드(타입별로 나눠서 전체를 보내줌)
    public ApiResponse<Map<ProdType, List<ProductResponseDto.GetProduct>>> productSearchAll() {
        List<Product> allProduct = productRepository.findAll();

        if (allProduct.isEmpty()) {
            throw new CustomException(PRODUCT_NOT_SEARCH);
        }

        Map<ProdType, List<ProductResponseDto.GetProduct>> responseMap = divisionProdByType(allProduct);

        return ApiResponse.success(PRODUCT_SEARCH_SUCCESS, responseMap);
    }

    public ApiResponse<List<ProductResponseDto.GetTopThree>> prodTopThree() {
        List<Product> getProdTopThree = productRepository.findTopProducts();

        if (getProdTopThree.isEmpty()) {
            throw new CustomException(PRODUCT_NOT_SEARCH);
        }

        List<ProductResponseDto.GetTopThree> productDtoList = new ArrayList<>();

        for (Product p : getProdTopThree) {
            ProductResponseDto.GetTopThree prodDto = ProductResponseDto.GetTopThree.from(p);
            productDtoList.add(prodDto);
        }

        return ApiResponse.success(PRODUCT_SEARCH_SUCCESS, productDtoList);
    }

    public ApiResponse<Map<ProdType, List<ProductResponseDto.GetProduct>>> getProdSearch(String keyword) {
        List<Product> getProdSearch = productRepository.findByProdNameContaining(keyword)
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_SEARCH));

        if (getProdSearch.isEmpty()) {
            throw new CustomException(PRODUCT_NOT_SEARCH);
        }

        Map<ProdType, List<ProductResponseDto.GetProduct>> responseMap = divisionProdByType(getProdSearch);

        return ApiResponse.success(PRODUCT_SEARCH_SUCCESS, responseMap);
    }

    // 타입별 분류
    private Map<ProdType, List<ProductResponseDto.GetProduct>> divisionProdByType(List<Product> listProduct) {
        return listProduct.stream()
                .collect(Collectors.groupingBy(
                        Product::getProdType,
                        Collectors.mapping(ProductResponseDto.GetProduct::from, Collectors.toList())
                ));
    }
}

