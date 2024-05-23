package com.hana.bankai.domain.product.enumtype;

import lombok.Getter;

// 적립 방식
@Getter
public enum ProdAcc {
    SIMPLE("단리"),
    COMPOUND("복리");

    private final String name;

    ProdAcc(String name) {
        this.name = name;
    }
}
