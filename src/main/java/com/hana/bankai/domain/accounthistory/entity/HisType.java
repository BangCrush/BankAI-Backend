package com.hana.bankai.domain.accounthistory.entity;

import lombok.Getter;

@Getter
public enum HisType {
    TRANSFER("이체"),
    AUTO_TRANSFER("자동 이체"),
    PAY_INTEREST("이자 지급");

    private final String desc;

    HisType(String desc) {
        this.desc = desc;
    }
}
