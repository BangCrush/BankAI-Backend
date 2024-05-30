package com.hana.bankai.account;

import com.hana.bankai.domain.account.dto.AccountResponseDto;
import com.hana.bankai.domain.account.service.AccountService;
import com.hana.bankai.global.common.enumtype.BankCode;
import com.hana.bankai.global.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.hana.bankai.domain.account.dto.AccountRequestDto.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class AccTransferTest {

    @Autowired
    AccountService accountService;

//    @Test
//    void 계좌이체_동시성_테스트() throws InterruptedException {
//        // given
//        ApiResponse<AccountResponseDto.GetBalance> outAcc = accountService.getBalance(AccCodeReq.builder().accCode("04-12345-1234567").build());
//        Long outAccBalance = outAcc.getData().getAccBalance();
//        ApiResponse<AccountResponseDto.GetBalance> inAcc = accountService.getBalance(AccCodeReq.builder().accCode("04-12345-7654321").build());
//        Long inAccBalance = inAcc.getData().getAccBalance();
//
//        Transfer transfer = Transfer.builder()
//                .inAccCode("04-12345-7654321")
//                .inBankCode(BankCode.C04)
//                .outAccCode("04-12345-1234567")
//                .outBankCode(BankCode.C04)
//                .amount(100L)
//                .build();
//
//        // when
//        int numberOfThreads = 5;
//        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
//        CountDownLatch latch = new CountDownLatch(numberOfThreads);
//
//        for (int i = 0; i < numberOfThreads; i++) {
//            executorService.submit(() -> {
//                try {
//                    accountService.transfer(transfer);
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//        latch.await();
//
//        // then
//        ApiResponse<AccountResponseDto.GetBalance> updatedOutAcc = accountService.getBalance(AccCodeReq.builder().accCode("04-12345-1234567").build());
//        Long updatedOutAccBalance = updatedOutAcc.getData().getAccBalance();
//        ApiResponse<AccountResponseDto.GetBalance> updatedInAcc = accountService.getBalance(AccCodeReq.builder().accCode("04-12345-7654321").build());
//        Long updatedInAccBalance = updatedInAcc.getData().getAccBalance();
//
//        assertThat(updatedOutAccBalance).isEqualTo(outAccBalance - 100L * numberOfThreads);
//        assertThat(updatedInAccBalance).isEqualTo(inAccBalance + 100L * numberOfThreads);
//
//        log.info("이체 전 출금 계좌 잔액 = {}", outAccBalance);
//        log.info("이체 전 입금 계좌 잔액 = {}", inAccBalance);
//        log.info("-----계좌 이체 진행-----");
//        log.info("이체 후 출금 계좌 잔액 = {}", updatedOutAccBalance);
//        log.info("이체 후 입금 계좌 잔액 = {}", updatedInAccBalance);
//    }
}
