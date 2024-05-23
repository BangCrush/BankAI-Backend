package com.hana.bankai.domain.product.enumtype;

import lombok.Getter;

// 상품 종류
@Getter
public enum ProdType {
    DEPOSIT("예금"),
    SAVINGS("적금"),
    LOAN("대출"),
    DEMAND_DEPOSIT("입출금");

    private final String name;

    ProdType(String name) {
        this.name = name;
    }
}
