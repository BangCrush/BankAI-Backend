package com.hana.bankai.domain.account.dto;

import com.hana.bankai.global.common.enumtype.BankCode;
import lombok.*;

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

        public static Transfer of(String inAccCode, BankCode inBankCode, String outAccCode, Long amount ) {
            return Transfer.builder()
                    .inAccCode(inAccCode)
                    .inBankCode(inBankCode)
                    .outAccCode(outAccCode)
                    .amount(amount)
                    .build();
        }
    }

    // 상품가입
    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ProdJoinReq {
        private Long prodCode;
        private Long amount;
        private Long accTrsfLimit; // 이체 한도
        private String outAccount; // 출금 계좌 (적금일 땐 자동 이체 계좌)
        private String accountPwd;
        private int period;
        // 자동 이체 (적금 또는 대출 상품일 때만 입력받음)
        private int atDate;
        private BankCode inBankCode;
    }

    // 이체한도 수정
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class TrsfLimitModify {
        private String accCode;
        private Long accTrsfLimit;
        private String accPwd;
    }

}
