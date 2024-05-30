package com.hana.bankai.domain.user.dto;

import lombok.*;

public class UserResponseDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    // 회원가입 시 중복 계정, 이메일, 아이디 여부 확인
    public static class RegisterDuplicateCheck {
        // 중복이면 true, 아니면 false 반환
        private Boolean check;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class TokenInfo {
        private String grantType;
        private String accessToken;
        private String refreshToken;
        private Long refreshTokenExpirationTime;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    // 아이디 찾기
    public static class LoginFindId {
        private String userId;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    // 비밀번호 찾기
    public static class LoginFindPwd {
        private String userPwd;
    }

}
