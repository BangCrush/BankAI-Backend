package com.hana.bankai.domain.account.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AccountRequestDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class AccCodeReq {
        private String accCode;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CheckTransferLimit {
        private String accCode;
        private Long amount;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CheckAccPwd {
        private String accCode;
        private String accPwd;
    }
}
