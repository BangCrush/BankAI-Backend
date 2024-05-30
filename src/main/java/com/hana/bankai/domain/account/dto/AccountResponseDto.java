package com.hana.bankai.domain.account.dto;

import com.hana.bankai.domain.accounthistory.entity.HisType;
import lombok.*;

import java.time.LocalDateTime;

public class AccountResponseDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class GetBalance {
        private Long accBalance;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class SearchAcc {
        private String accCode;
        private String userName;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class CheckRes {
        private Boolean check;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class getAccHis {
        private LocalDateTime hisDateTime;
        private HisType hisType;
        private String target;
        private Long hisAmount;
        private Long balance;
    }
}
