package com.hana.bankai.domain.user.dto;

import com.hana.bankai.domain.user.entity.User;
import com.hana.bankai.domain.user.entity.UserJob;
import lombok.*;

public class UserResponseDto {

    // 회원가입 시 중복 계정, 이메일, 아이디 여부 확인
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
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

    // 아이디 찾기
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class LoginFindId {
        private String userId;
    }

    // 임시 비밀번호 발급
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class LoginTempPwd {
        private String userPwd;
    }

    // 회원 개인 정보 조회
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class UserInfo {
        private String userNameKr;
        private String userNameEn;
        private String userPhone;
        private String userEmail;
        private String userAddr;
        private String userAddrDetail;
        private String jobName;
        private String companyName;
        private String companyAddr;
        private String companyPhone;
        private String userMainAcc;
        private Long userTrsfLimit;

        public static UserInfo from(User user) {
            return UserInfo.builder()
                    .userNameKr(user.getUserNameKr())
                    .userNameEn(user.getUserNameEn())
                    .userPhone(user.getUserPhone())
                    .userEmail(user.getUserEmail())
                    .userAddr(user.getUserAddr())
                    .userAddrDetail(user.getUserAddrDetail())
                    .jobName(user.getUserJob() != null ? user.getUserJob().getJobName() : null)
                    .companyName(user.getUserJob() != null ? user.getUserJob().getCompanyName() : null)
                    .companyAddr(user.getUserJob() != null ? user.getUserJob().getCompanyAddr() : null)
                    .companyPhone(user.getUserJob() != null ? user.getUserJob().getCompanyPhone() : null)
                    .userMainAcc(user.getUserMainAcc() != null ? user.getUserMainAcc() : null)
                    .userTrsfLimit(user.getUserTrsfLimit().getDailyLimit())
                    .build();
        }
    }

    // 회원 직업 정보 조회
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class UserJobInfo {
        private String jobName;
        private String companyName;
        private String companyAddr;
        private String companyPhone;

        public static UserJobInfo from(UserJob userJob) {
            // 직업 정보가 없으면 null return
            if(userJob.getJobName() == null && userJob.getCompanyName() == null && userJob.getCompanyAddr() == null && userJob.getCompanyPhone() == null) {
                return null;
            }

            return UserJobInfo.builder()
                    .jobName(userJob.getJobName())
                    .companyName(userJob.getCompanyName())
                    .companyAddr(userJob.getCompanyAddr())
                    .companyPhone(userJob.getCompanyPhone())
                    .build();
        }
    }

}
