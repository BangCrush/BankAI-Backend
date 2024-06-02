package com.hana.bankai.domain.account.dto;


import com.hana.bankai.global.common.enumtype.BankCode;
import lombok.*;

import com.hana.bankai.domain.account.entity.Account;
import lombok.*;
import lombok.extern.java.Log;

import java.util.UUID;

public class AccountRequestDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CheckTransferLimit {
        private String accCode;
        private Long amount;
    }
    // 계좌해지 및 계좌 비밀번호 확인
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class CheckAccPwd {
        private String accCode;
        private String accPwd;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Transfer {
        private String inAccCode;
        private BankCode inBankCode;
        private String outAccCode;
        private BankCode outBankCode;
        private Long amount;
    }
    // 상품가입
    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ProdJoinReq {
        private Long prodCode;
        private Long amount;
        private int period;
        private String outAccount;
        private int autoTransferte;
        private String accountPwd;
        private Long accTrsfLimit;
    }

    // 이체한도 수정
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class TrsfLimitModify {
        private String accCode;
        private Long accTrsfLimit;
        private String accPwd;
    };
}
