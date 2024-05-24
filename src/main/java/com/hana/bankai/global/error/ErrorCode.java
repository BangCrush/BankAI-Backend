package com.hana.bankai.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // User
    USER_NOT_FOUND("존재하지 않는 회원입니다."),

    // Account
    ACCOUNT_NOT_FOUND("존재하지 않는 계좌입니다.")
    ;
    private final String message;
}
