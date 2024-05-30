package com.hana.bankai.domain.product.entity;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ProdType {
    DEPOSIT("예금",1),
    SAVINGS("적금",2),
    LOAN("대출",3);

    private final String desc;
    private final int code;


    ProdType(String desc, int code) {
        this.desc = desc;
        this.code = code;
    }
    public static ProdType of(final int code){
        return Arrays.stream(values())
                .filter(val -> code == val.code)
                .findFirst()
                .orElse(null);
    }
}
