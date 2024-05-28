package com.hana.bankai.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // User
    USER_NOT_FOUND("E100", "존재하지 않는 회원입니다."),

    // Account
    ACCOUNT_NOT_FOUND("E200", "존재하지 않는 계좌입니다."),
    INSUFFICIENT_BALANCE("E201", "계좌 잔액이 부족합니다."),
    INVALID_TRANSFER_AMOUNT("E202", "유효하지 않은 이체 금액입니다."),
    ACCOUNT_TRANSFER_FAIL("E203", "계좌이체 실패")
    ;

    private final String code;
    private final String message;
}
