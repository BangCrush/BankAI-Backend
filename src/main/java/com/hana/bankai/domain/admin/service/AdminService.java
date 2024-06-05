package com.hana.bankai.domain.admin.service;

import com.hana.bankai.domain.account.repository.AccountRepository;
import com.hana.bankai.domain.admin.dto.AdminRequestDto;
import com.hana.bankai.domain.product.entity.ProdType;
import com.hana.bankai.domain.admin.dto.AdminResponseDto;
import com.hana.bankai.domain.product.entity.ProdType;
import com.hana.bankai.domain.product.entity.Product;
import com.hana.bankai.domain.product.repsoitory.ProductRepository;
import com.hana.bankai.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.hana.bankai.global.common.response.SuccessCode.PRODUCT_SEARCH_SUCCESS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.hana.bankai.domain.product.entity.ProdType.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;

    public Map<String, Map<Integer, Integer>> getLineChartData(int year) {
        Map<String, Map<Integer, Integer>> datas = new HashMap<>();

        // 입출금
        datas.put(CHECKING.getDesc(), transformData(accountRepository.findMonthlyAccountCounts(year, CHECKING)));
        // 예금
        datas.put(DEPOSIT.getDesc(), transformData(accountRepository.findMonthlyAccountCounts(year, DEPOSIT)));
        // 적금
        datas.put(SAVINGS.getDesc(), transformData(accountRepository.findMonthlyAccountCounts(year, SAVINGS)));
        // 대출
        datas.put(LOAN.getDesc(), transformData(accountRepository.findMonthlyAccountCounts(year, LOAN)));

        return datas;
    }

    private Map<Integer, Integer> transformData(List<Object[]> rawData) {
        Map<Integer, Integer> transformedData = new HashMap<>();
        for (Object[] data : rawData) {
            Integer month = (Integer) data[0];
            Integer count = ((Number) data[1]).intValue();
            transformedData.put(month, count);
        }
        return transformedData;
    }

    public Map<ProdType, List<AdminResponseDto.ProductJoinCntByAgeGroup>> getBarChartData() {
        List<Product> ProductList = productRepository.findAll();

        Map<ProdType, List<AdminResponseDto.ProductJoinCntByAgeGroup>> response = divisionProdByType(ProductList);

        return response;
    }

    private Map<ProdType, List<AdminResponseDto.ProductJoinCntByAgeGroup>> divisionProdByType(List<Product> productList) {
        return productList.stream()
                .collect(Collectors.groupingBy(
                        Product::getProdType,
                        Collectors.mapping(AdminResponseDto.ProductJoinCntByAgeGroup::from, Collectors.toList())
                ));
    }

    // 상품별 가입자수 비율
    public ApiResponse<List<Integer>> getProdJoinRate() {
        List<Integer> productDtoList =  new ArrayList<>();
        for(int i=1; i <= 4; i++){
            ProdType prodTypeNum = ProdType.of(i);
            int list = accountRepository.countByProdType(prodTypeNum);
            productDtoList.add(list);
        }
        return ApiResponse.success(PRODUCT_SEARCH_SUCCESS, productDtoList);
    };
}
