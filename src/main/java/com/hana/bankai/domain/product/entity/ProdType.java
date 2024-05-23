package com.hana.bankai.domain.product.entity;

import lombok.Getter;

@Getter
public enum ProdType {
    DEPOSIT("예금"),
    SAVINGS("적금"),
    LOAN("대출"),
    DEMAND_DEPOSIT("입출금");

    private final String desc;

    ProdType(String desc) {
        this.desc = desc;
    }
}
