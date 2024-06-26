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
    public static class GetProduct {
        private Long prodCode;
        private String prodName;
        private String prodPromo;
        private int joinPeriod;
        private double prodRate;

        // from 메소드 정의
        public static ProductResponseDto.GetProduct from(Product prodEntity) {
            return new ProductResponseDto.GetProduct(
                    prodEntity.getProdCode(),
                    prodEntity.getProdName(),
                    prodEntity.getProdPromo(),
                    prodEntity.getJoinPeriod(),
                    prodEntity.getProdRate()
            );
        }
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
        private String prodPromo;

        // from 메소드 정의
        public static ProductResponseDto.GetTopThree from(Product prodEntity) {
            return new ProductResponseDto.GetTopThree(
                    prodEntity.getProdCode(),
                    prodEntity.getProdName(),
                    prodEntity.getJoinPeriod(),
                    prodEntity.getProdRate(),
                    prodEntity.getProdLimit(),
                    prodEntity.getProdPromo()
            );
        }
    }
}
