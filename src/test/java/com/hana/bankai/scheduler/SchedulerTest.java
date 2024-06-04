package com.hana.bankai.scheduler;

import com.hana.bankai.domain.user.service.UserTrsfLimitService;
import com.hana.bankai.global.scheduler.BankaiScheduler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class SchedulerTest {

//    @Mock
//    private UserTrsfLimitService trsfLimitService;
//    @InjectMocks
//    private BankaiScheduler scheduler;
//
//    @Test
//    public void 사용자_이체한도_초기화_테스트() {
//        Mockito.doNothing().when(trsfLimitService).resetUserDailyTrsfLimit();
//        scheduler.resetDailyUserTrsfLimit();
//        Mockito.verify(trsfLimitService, Mockito.times(1)).resetUserDailyTrsfLimit();
//    }
}