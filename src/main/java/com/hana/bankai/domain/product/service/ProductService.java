package com.hana.bankai.domain.product.service;

import com.hana.bankai.domain.account.dto.AccountRequestDto;
import com.hana.bankai.domain.account.dto.AccountResponseDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.hana.bankai.global.common.response.SuccessCode.ACCOUNT_BALANCE_CHECK_SUCCESS;
import static com.hana.bankai.global.common.response.SuccessCode.PRODUCT_SEARCH_SUCCESS;
import static com.hana.bankai.global.error.ErrorCode.ACCOUNT_NOT_FOUND;
import static com.hana.bankai.global.error.ErrorCode.PRODUCT_NOT_SEARCH;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    public ApiResponse<ProductResponseDto.GetProductDetail> getProductDetail(Long prodCode) {

        Product p = productRepository.findById(prodCode)
                .orElseThrow(() -> new CustomException(ACCOUNT_NOT_FOUND));
        ProductResponseDto.GetProductDetail dto = ProductResponseDto.GetProductDetail.builder()
        .prodCode(p.getProdCode())
        .prodType(p.getProdType())
        .prodName(p.getProdName())
        .prodDesc(p.getProdDesc())
        .joinPeriod(p.getJoinPeriod())
        .prodRate(p.getProdRate())
        .prodMin(p.getProdMin())
        .prodMax(p.getProdMax())
        .joinMember(p.getJoinMember())
        .prodLimit(p.getProdLimit())
        .prodRateMthd(p.getProdRateMthd())
        .prodRepay(p.getProdRepay())
        .prodAcc(p.getProdAcc())
        .prodPromo(p.getProdPromo())
        .prodTerms(p.getProdTerms()).build();

        return ApiResponse.success(PRODUCT_SEARCH_SUCCESS, dto);
    }

    public ApiResponse<List<ProductResponseDto.GetProduct>> getProduct(int prodType) {
        ProdType prodTypeNum = ProdType.of(prodType);
        List<Product> productList = productRepository.findByProdType(prodTypeNum) ;
        if (productList.size() == 0){
            throw new CustomException(PRODUCT_NOT_SEARCH);
        }
        List<ProductResponseDto.GetProduct> productDtoList =  new ArrayList<>();
        for(Product p : productList){ // 3
            ProductResponseDto.GetProduct dto = ProductResponseDto.GetProduct.builder()
                    .prodCode(p.getProdCode())
                    .prodName(p.getProdName())
                    .prodPromo(p.getProdPromo())
                    .joinMember(p.getJoinMember())
                    .prodRate(p.getProdRateMthd())
                    .prodLimit(p.getProdLimit())
                    .build();
            productDtoList.add(dto); // 4
        }
        return ApiResponse.success(PRODUCT_SEARCH_SUCCESS, productDtoList); // Pass the product object
    }
    public ApiResponse<Map<ProdType, List<Product>>> productSearchAll() {
        List<Product> allProduct = productRepository.findAll();
        Map<ProdType,List<Product>> typeGroupProduct = allProduct.stream()
                .collect(Collectors.groupingBy(Product::getProdType));
        return ApiResponse.success(PRODUCT_SEARCH_SUCCESS, typeGroupProduct);

    }
}

