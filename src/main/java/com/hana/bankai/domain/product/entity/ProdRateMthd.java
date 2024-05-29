package com.hana.bankai.domain.product.entity;

import lombok.Getter;

@Getter
public enum ProdRateMthd {
    EXPIRYDATE("만기일시지급식: 만기(후) 해지시 원금과 함께 지급"),
    NONE("없음"); // 대출은 없음 없어서 넣었다.

    private final String desc;


    ProdRateMthd(String desc) {
        this.desc = desc;
    }
}
