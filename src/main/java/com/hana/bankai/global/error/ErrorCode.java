package com.hana.bankai.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // User
    USER_NOT_FOUND("E100", "존재하지 않는 회원입니다."),
    USER_LOGIN_INCORRECT("E101", "아이디 또는 비밀번호가 다릅니다."),
    USER_REGISTER_VALIDATION_FAIL("E102", "비밀번호 또는 이메일 형식이 올바르지 않습니다."),
    USER_LOGOUT_ACCESS_TOKEN_VALIDATION_FAIL("E103", "잘못된 요청입니다."),
    USER_TRSF_LIMIT_NOT_FOUND("E104", "사용자 일일 이체 한도 조회 불가"),
    USER_AUTHENTICATION_FAIL("E105", "사용자 인증 실패"),
    USER_GET_INFO_FAIL("E106", "회원 정보 조회 실패"),
    USER_UPDATE_INFO_FAIL("E107", "회원 정보 수정 실패"),
    USER_GET_JOB_INFO_FAIL("E108", "직업 정보 조회 실패"),
    USER_UPDATE_JOB_INFO_FAIL("E109", "직업 정보 수정 실패"),
    SEND_EMAIL_CODE_FAIL("E110", "이메일 인증 코드 전송 실패"),
    USER_ACCESS_FAIL("E111", "사용자 인가 실패"),
    USER_REISSUE_FAIL("E112", "토큰 재발급 실패"),

    // Account
    ACCOUNT_NOT_FOUND("E200", "존재하지 않는 계좌입니다."),
    INSUFFICIENT_BALANCE("E201", "계좌 잔액이 부족합니다."),
    INVALID_TRANSFER_AMOUNT("E202", "유효하지 않은 이체 금액입니다."),
    ACCOUNT_TRANSFER_FAIL("E203", "계좌이체 실패"),
    TRANSFER_LIMIT_EXCEEDED("E204", "이체한도 초과"),
    FIND_ACCOUNT_HISTORY_FAIL("E205", "거래내역 조회 실패"),
    LIMIT_MODIFY_FAIL("E206","이체 한도 수정 실패: 이체 한도가 일일 한도보다 큽니다"),
    ACCOUNT_PWD_FAIL("E207","계좌 비밀번호가 다릅니다"),

    // Redis
    LOCK_NOT_AVAILABLE("E400", "사용할 수 없는 락"),
    LOCK_INTERRUPTED_ERROR("E401", "락 획득 시 인터럽트 발생"),

    // Product
    PRODUCT_NOT_SEARCH("E300","해당하는 상품을 찾을 수 없습니다")
    ;

    private final String code;
    private final String message;
}
