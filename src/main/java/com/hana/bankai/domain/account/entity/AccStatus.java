package com.hana.bankai.domain.account.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum AccStatus {
    ACTIVE("활성"),
    DELETED("삭제");

    private final String desc;

    AccStatus(String desc) {
        this.desc = desc;
    }
}
