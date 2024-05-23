package com.hana.bankai.domain.account.enumtype;

import lombok.Getter;

// 활성화 여부
@Getter
public enum AccStatus {
    ACTIVE("활성"),
    DELETED("삭제");

    private final String name;

    AccStatus(String name) {
        this.name = name;
    }
}
