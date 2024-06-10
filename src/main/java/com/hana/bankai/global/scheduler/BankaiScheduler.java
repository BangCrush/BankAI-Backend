package com.hana.bankai.global.scheduler;

import com.hana.bankai.domain.account.service.AccountService;
import com.hana.bankai.domain.autotransfer.service.AutoTransferService;
import com.hana.bankai.domain.user.service.UserTrsfLimitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BankaiScheduler {

    private final UserTrsfLimitService trsfLimitService;
    private final AutoTransferService autoTransferService;
    private final AccountService accountService;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void resetDailyUserTrsfLimit() {
        trsfLimitService.resetUserDailyTrsfLimit();
    }

    @Scheduled(cron = "0 0 15 * * ?") // 매일 낮 15시에 실행
    public void autoTransfer() { autoTransferService.autoTransfer(); }

    @Scheduled(cron = "0 6 14 10 * ?") // 매달 10일 낮 12시에 실행
    public void rateTransfer() { accountService.rateTransfer(); }
}
