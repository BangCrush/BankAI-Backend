package com.hana.bankai.domain.product.entity;

import lombok.Getter;

@Getter
public enum ProdAcc {
    SIMPLE("단리"),
    COMPOUND("복리");

    private final String desc;

    ProdAcc(String desc) {
        this.desc = desc;
    }
}
