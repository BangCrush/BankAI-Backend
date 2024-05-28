package com.hana.bankai.domain.product.dto;

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
        private String prodRate;
        private Long prodLimit;
    }


}
