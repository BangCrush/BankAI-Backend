package com.hana.bankai.domain.product.service;

import com.hana.bankai.domain.product.dto.ProductResponseDto;
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

import static com.hana.bankai.global.common.response.SuccessCode.PRODUCT_SEARCH_SUCCESS;
import static com.hana.bankai.global.error.ErrorCode.PRODUCT_NOT_SEARCH;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public ApiResponse<List<ProductResponseDto.GetProduct>> getProduct(String prodTypeNum) {
        List<Product> productList = productRepository.findByProdType(prodTypeNum) ;
        if (productList.size() == 0){
            throw new CustomException(PRODUCT_NOT_SEARCH);
        }
        List<ProductResponseDto.GetProduct> productDtoList =  new ArrayList<>();
        for(Product pdl : productList){ // 3
            ProductResponseDto.GetProduct dto = ProductResponseDto.GetProduct.builder()
                    .prodCode(pdl.getProdCode())
                    .prodName(pdl.getProdName())
                    .prodPromo(pdl.getProdPromo())
                    .joinMember(pdl.getJoinMember())
                    .prodRate(pdl.getProdRateMthd())
                    .prodLimit(pdl.getProdLimit())
                    .build();
            productDtoList.add(dto); // 4
        }

        return ApiResponse.success(PRODUCT_SEARCH_SUCCESS, productDtoList); // Pass the product object
    }
}

