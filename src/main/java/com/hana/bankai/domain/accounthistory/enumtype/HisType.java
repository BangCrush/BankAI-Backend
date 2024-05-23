package com.hana.bankai.domain.accounthistory.enumtype;

import lombok.Getter;

// 거래 유형
@Getter
public enum HisType {
    TRANSFER("이체"),
    AUTO_TRANSFER("자동 이체");

    private final String name;

    HisType(String name) {
        this.name = name;
    }
}
