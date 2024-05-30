package com.hana.bankai.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // User
    USER_NOT_FOUND("E100", "존재하지 않는 회원입니다."),
    USER_LOGIN_INCORRECT("E101", "아이디 또는 비밀번호가 다릅니다"),

    // Account
    ACCOUNT_NOT_FOUND("E200", "존재하지 않는 계좌입니다."),

    //Product
    PRODUCT_NOT_SEARCH("E300","해당하는 상품을 찾을 수 없습니다")
    ;

    private final String code;
    private final String message;
}
