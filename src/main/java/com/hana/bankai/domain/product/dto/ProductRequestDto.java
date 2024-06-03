package com.hana.bankai.domain.product.dto;

import lombok.*;

public class ProductRequestDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ProductSearch {
        private String keyword;
    };
}
