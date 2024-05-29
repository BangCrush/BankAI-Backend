package com.hana.bankai.domain.product.entity;

import lombok.Getter;

@Getter
public enum ProdRepay {
    DEFERRED("거치식 상환"),
    BULLET("만기일시상환"),
    EQUAL_INSTALLMENT("원리금균등상환"),
    EQUAL_PRINCIPAL("원금균등분할상환"),
    NONE("없음"); // 예금, 적금은 없어서 넣었다.

    private final String desc;

    ProdRepay(String desc) {
        this.desc = desc;
    }
}
