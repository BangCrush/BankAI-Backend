package com.hana.bankai.domain.admin.dto;

import com.hana.bankai.domain.product.entity.ProdAcc;
import com.hana.bankai.domain.product.entity.ProdRateMthd;
import com.hana.bankai.domain.product.entity.ProdRepay;
import com.hana.bankai.domain.product.entity.ProdType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminRequestDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ProductSearch {
        private Long prodCode;
        private String joinMember;
        private int joinPeriod;
        private ProdAcc prodAcc;
        private String prodDesc;
        private Long prodLimit;
        private Long prodMax;
        private Long prodMin;
        private String prodName;
        private String prodPromo;
        private double prodRate;
        private ProdRateMthd prodRateMthd;
        private ProdRepay prodRepay;
        private String prodTerms;
        private ProdType prodType;
    };
}
