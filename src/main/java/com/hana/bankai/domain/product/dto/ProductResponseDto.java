package com.hana.bankai.domain.product.dto;

import com.hana.bankai.domain.product.entity.ProdAcc;
import com.hana.bankai.domain.product.entity.ProdRepay;
import com.hana.bankai.domain.product.entity.ProdType;
import com.hana.bankai.domain.product.entity.Product;
import lombok.*;


public class ProductResponseDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class GetProduct {
        private Long prodCode;
        private String prodName;
        private String prodPromo;
        private String joinMember;
        private double prodRate;
        private Long prodLimit;
    }
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class GetProductDetail {
        private Long prodCode;
        private ProdType prodType;
        private String prodName;
        private String prodDesc;
        private int joinPeriod;
        private double prodRate;
        private Long prodMin;
        private Long prodMax;
        private String joinMember;
        private Long prodLimit;
        private String prodRateMthd;
        private ProdRepay prodRepay;
        private String prodCaution;
        private ProdAcc prodAcc;
        private String prodPromo;
        private String prodTerms;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class GetTopThree {
        private Long prodCode;
        private String prodName;
        private int joinPeriod;
        private double prodRate;
        private Long prodLimit;
        private String prodDesc;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class GetProdSearch {
        private Long prodCode;
        private String prodName;
        private String prodDesc;
        private int joinPeriod;
        private double prodRate;
        private Long prodLimit;

        // from 메소드 정의
        public static ProductResponseDto.GetProdSearch from(Product prodEntity) {
            return new ProductResponseDto.GetProdSearch(
                    prodEntity.getProdCode(),
                    prodEntity.getProdName(),
                    prodEntity.getProdDesc(),
                    prodEntity.getJoinPeriod(),
                    prodEntity.getProdRate(),
                    prodEntity.getProdLimit()
                    );
        }

    }

}
