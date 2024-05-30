package com.hana.bankai.domain.account.dto;

import com.hana.bankai.global.common.enumtype.BankCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AccountRequestDto {

    @Getter
    @Builder
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

    @Getter
    @Builder
    public static class Transfer {
        private String inAccCode;
        private BankCode inBankCode;
        private String outAccCode;
        private BankCode outBankCode;
        private Long amount;
    }
}
