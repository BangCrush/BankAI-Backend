package com.hana.bankai.global.scheduler;

import com.hana.bankai.domain.autotransfer.service.AutoTransferService;
import com.hana.bankai.domain.user.service.UserTrsfLimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BankaiScheduler {

    private final UserTrsfLimitService trsfLimitService;
    private final AutoTransferService autoTransferService;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void resetDailyUserTrsfLimit() {
        trsfLimitService.resetUserDailyTrsfLimit();
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void autoTransfer() { autoTransferService.autoTransfer(); }
}
