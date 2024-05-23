package com.hana.bankai.domain.product.enumtype;

import lombok.Getter;

// 상환방식
@Getter
public enum ProdRepay {
    DEFERRED("거치식 상환"),          // 거치식상환
    BULLET("만기일시상환"),            // 만기일시상환
    EQUAL_INSTALLMENT("원리금균등상환"),
    EQUAL_PRINCIPAL("원금균등분할상환");

    private final String name;

    ProdRepay(String name) {
        this.name = name;
    }
}
