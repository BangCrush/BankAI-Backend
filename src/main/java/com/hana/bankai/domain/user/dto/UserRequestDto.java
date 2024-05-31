package com.hana.bankai.domain.user.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class UserRequestDto {

    /* register */

    @Getter
    // 회원가입 여부 확인
    public static class RegisterCheck {
        @NotEmpty(message = "주민등록번호는 필수 입력값입니다.")
        private String userInherentNumber;
    }

    @Getter
    // 중복 이메일 확인
    public static class RegisterCheckEmail {
        @NotEmpty(message = "이메일는 필수 입력값입니다.")
        private String userEmail;
    }

    @Getter
    // 중복 ID 확인
    public static class RegisterCheckId {
        @NotEmpty(message = "아이디는 필수 입력값입니다.")
        private String userId;
    }

    @Getter
    @Builder
    public static class Register {
        @NotEmpty(message = "ID를 입력하세요.")
        private String userId;

        @NotEmpty(message = "비밀번호를 입력하세요.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String userPwd;

        @NotEmpty(message = "이름을 입력하세요.")
        private String userNameKr;

        @NotEmpty(message = "영문이름을 입력하세요.")
        private String userNameEn;

        @NotEmpty(message = "이메일을 입력하세요.")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        private String userEmail;

        @NotEmpty(message = "주민등록번호를 입력하세요.")
        private String userInherentNumber;

        @NotEmpty(message = "휴대폰 번호를 입력하세요.")
        private String userPhone;

        @NotEmpty(message = "주소를 입력하세요.")
        private String userAddr;

        @NotEmpty(message = "상세주소를 입력하세요.")
        private String userAddrDetail;
    }

    /* login */

    @Getter
    public static class Login {
        @NotEmpty(message = "아이디를 입력하세요.")
        private String userId;

        @NotEmpty(message = "비밀번호를 입력하세요.")
        private String userPwd;

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(userId, userPwd);
        }

    }

    @Getter
    @Setter
    public static class Reissue {
        @NotEmpty(message = "accessToken 을 입력해주세요.")
        private String accessToken;

        @NotEmpty(message = "refreshToken 을 입력해주세요.")
        private String refreshToken;
    }

    @Getter
    @Setter
    // 아이디 찾기
    public static class LoginFindId {
        @NotEmpty(message = "이름을 입력하세요.")
        private String userNameKr;

        @NotEmpty(message = "이메일을 입력하세요.")
        private String userEmail;
    }

    @Getter
    @Setter
    // 비밀번호 찾기
    public static class LoginFindPwd {
        @NotEmpty(message = "이름을 입력하세요.")
        private String userNameKr;

        @NotEmpty(message = "아이디를 입력하세요.")
        private String userId;

        @NotEmpty(message = "이메일을 입력하세요.")
        private String userEmail;
    }

    @Getter
    @Setter
    public static class Logout {
        @NotEmpty(message = "잘못된 요청입니다.")
        private String accessToken;

        @NotEmpty(message = "잘못된 요청입니다.")
        private String refreshToken;
    }

}
