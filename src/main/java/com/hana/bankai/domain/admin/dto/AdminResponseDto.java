package com.hana.bankai.domain.admin.dto;

import com.hana.bankai.domain.admin.entity.JoinCnt;
import com.hana.bankai.domain.product.entity.Product;
import lombok.*;

public class AdminResponseDto {

    // 연령대별 각 상품 가입자 수
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ProductJoinCntByAgeGroup {
        private String prodName;
        private JoinCnt joinCnt;

        public static AdminResponseDto.ProductJoinCntByAgeGroup from(Product product) {
            return new ProductJoinCntByAgeGroup(product.getProdName(), new JoinCnt(product));
        }

    }

}
