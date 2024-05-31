package com.hana.bankai.domain.product.entity;

import lombok.Getter;

@Getter
public enum ProdRepay {
    BULLET("만기일시상환"),
    EQUAL_INSTALLMENT("원리금균등상환"),
    NONE("없음"); // 예금, 적금은 없어서 넣었다.

    private final String desc;

    ProdRepay(String desc) {
        this.desc = desc;
    }
}
