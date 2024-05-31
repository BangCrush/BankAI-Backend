package com.hana.bankai.domain.product.entity;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ProdType {
    CHECKING("입출금",1),
    DEPOSIT("예금",2),
    SAVINGS("적금",3),
    LOAN("대출",4);

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
