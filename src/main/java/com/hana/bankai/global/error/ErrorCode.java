package com.hana.bankai.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // User
    USER_NOT_FOUND("E100", "존재하지 않는 회원입니다."),
    USER_TRSF_LIMIT_NOT_FOUND("E101", "사용자 일일 이체 한도 조회 불가"),

    // Account
    ACCOUNT_NOT_FOUND("E200", "존재하지 않는 계좌입니다."),
    INSUFFICIENT_BALANCE("E201", "계좌 잔액이 부족합니다."),
    INVALID_TRANSFER_AMOUNT("E202", "유효하지 않은 이체 금액입니다."),
    ACCOUNT_TRANSFER_FAIL("E203", "계좌이체 실패"),
    TRANSFER_LIMIT_EXCEEDED("E204", "이체한도 초과"),
    FIND_ACCOUNT_HISTORY_FAIL("E205", "거래내역 조회 실패"),

    // Redis
    LOCK_NOT_AVAILABLE("E400", "사용할 수 없는 락"),
    LOCK_INTERRUPTED_ERROR("E401", "락 획득 시 인터럽트 발생"),

    //Product
    PRODUCT_NOT_SEARCH("E300","해당하는 상품을 찾을 수 없습니다")
    ;

    private final String code;
    private final String message;
}
