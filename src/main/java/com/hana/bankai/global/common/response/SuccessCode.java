package com.hana.bankai.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    // User
    USER_LOGIN_SUCCESS(OK, "로그인 성공"),

    // Account
    ACCOUNT_BALANCE_CHECK_SUCCESS(OK, "계좌 잔액 조회 성공"),
    ACCOUNT_SEARCH_SUCCESS(OK, "계좌 조회 성공"),
    ACCOUNT_LIMIT_CHECK_SUCCESS(OK, "이체한도 확인")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusValue() {
        return httpStatus.value();
    }
}
