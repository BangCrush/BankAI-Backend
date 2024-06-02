package com.hana.bankai.domain.product.service;

import com.hana.bankai.domain.account.dto.AccountRequestDto;
import com.hana.bankai.domain.account.dto.AccountResponseDto;
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
    private final AccountRepository accountRepository;
    
    // 상품 디테일 페이지
    public ApiResponse<ProductResponseDto.GetProductDetail> getProductDetail(Long prodCode) {

        Product p = productRepository.findById(prodCode)
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_SEARCH));
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
    };

    // 상품 별 전체 조회 코드
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
                    .prodRate(p.getProdRate())
                    .prodLimit(p.getProdLimit())
                    .build();
            productDtoList.add(dto); // 4
        }
        return ApiResponse.success(PRODUCT_SEARCH_SUCCESS, productDtoList); // Pass the product object
    };

    // 전체 조회 코드(타입별로 나눠서 전체를 보내줌)
    public ApiResponse<Map<ProdType, List<Product>>> productSearchAll() {
        List<Product> allProduct = productRepository.findAll();
        Map<ProdType,List<Product>> typeGroupProduct = allProduct.stream()
                .collect(Collectors.groupingBy(Product::getProdType));
        return ApiResponse.success(PRODUCT_SEARCH_SUCCESS, typeGroupProduct);

    };

    public ApiResponse<List<ProductResponseDto.GetTopThree>> prodTopThree(){
        List<Product> getProdTopThree = productRepository.findTopProducts();

        if (getProdTopThree.size() == 0){
            throw new CustomException(PRODUCT_NOT_SEARCH);
        }
        List<ProductResponseDto.GetTopThree> productDtoList =  new ArrayList<>();
        for(Product p : getProdTopThree){ // 3
            ProductResponseDto.GetTopThree dto = ProductResponseDto.GetTopThree.builder()
                    .prodCode(p.getProdCode())
                    .prodName(p.getProdName())
                    .prodRate(p.getProdRate())
                    .prodLimit(p.getProdLimit())
                    .joinPeriod(p.getJoinPeriod())
                    .prodDesc(p.getProdDesc())
                    .build();
            productDtoList.add(dto); // 4
        }
        return ApiResponse.success(PRODUCT_SEARCH_SUCCESS,productDtoList);
    }
}

