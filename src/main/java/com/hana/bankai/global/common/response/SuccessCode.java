package com.hana.bankai.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    // User
    USER_REGISTER_CHECK_SUCCESS(OK, "회원가입 여부 확인 성공"),
    USER_REGISTER_CHECK_EMAIL_SUCCESS(OK, "이메일 중복 확인 성공"),
    USER_REGISTER_CHECK_ID_SUCCESS(OK, "아이디 중복 확인 성공"),
    USER_REGISTER_SUCCESS(OK, "회원가입 성공"),
    USER_LOGIN_SUCCESS(OK, "로그인 성공"),
    USER_FIND_ID_SUCCESS(OK, "아이디 찾기 성공"),
    USER_FIND_PWD_SUCCESS(OK, "비밀번호 찾기 성공"),
    USER_LOGOUT_SUCCESS(OK, "로그아웃 성공"),
    USER_GET_INFO_SUCCESS(OK, "회원 정보 조회 성공"),
    USER_UPDATE_INFO_SUCCESS(OK, "회원 정보 수정 성공"),

    // Account
    ACCOUNT_BALANCE_CHECK_SUCCESS(OK, "계좌 잔액 조회 성공"),
    ACCOUNT_SEARCH_SUCCESS(OK, "계좌 조회 성공"),
    ACCOUNT_LIMIT_CHECK_SUCCESS(OK, "이체한도 확인"),
    ACCOUNT_PWD_CHECK_SUCCESS(OK, "계좌 비밀번호 확인"),
    ACCOUNT_TRANSFER_SUCCESS(CREATED, "계좌이체 성공"),
    ACCOUNT_HISTORY_CHECK_SUCCESS(OK, "거래내역 조회 성공"),
    ACCOUNT_LIST_CHECK_SUCCESS(OK, "계좌 잔액 조회 성공"),
    USER_ASSETS_CHECK_SUCCESS(OK, "사용자 자산 조회 성공"),

    // Product
    PRODUCT_SEARCH_SUCCESS(OK, "상품 조회 성공")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusValue() {
        return httpStatus.value();
    }
}
