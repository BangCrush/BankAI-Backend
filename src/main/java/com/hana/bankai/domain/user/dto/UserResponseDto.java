package com.hana.bankai.domain.user.dto;

import lombok.*;

public class UserResponseDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    // 회원가입 여부 확인
    public static class RegisterCheck {
        private Boolean check;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    // 중복 이메일 확인
    public static class RegisterCheckEmail {
        private Boolean check;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    // 중복 아이디 확인
    public static class RegisterCheckId {
        private Boolean check;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class Login {
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
        private String userName;
        private String userId;
        private String userEmail;
    }

    // !! 임시 코드. 추후 삭제 예정
    @Builder
    @Getter
    @AllArgsConstructor
    public static class TokenInfo {
        private String grantType;
        private String accessToken;
        private String refreshToken;
        private Long refreshTokenExpirationTime;
    }

}
