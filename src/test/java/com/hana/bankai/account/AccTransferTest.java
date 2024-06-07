package com.hana.bankai.account;

import com.hana.bankai.domain.account.dto.AccountResponseDto;
import com.hana.bankai.domain.account.service.AccountService;
import com.hana.bankai.domain.accounthistory.entity.HisType;
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
//        ApiResponse<AccountResponseDto.GetBalance> outAcc1 = accountService.getBalance("04-23952-3659537", "soyeon");
//        Long outAcc1Balance = outAcc1.getData().getAccBalance();
//        ApiResponse<AccountResponseDto.GetBalance> outAcc2 = accountService.getBalance("04-00706-3829355", "sojisub");
//        Long outAcc2Balance = outAcc2.getData().getAccBalance();
//        ApiResponse<AccountResponseDto.GetBalance> inAcc = accountService.getBalance("04-77825-0567386", "samsiclover");
//        Long inAccBalance = inAcc.getData().getAccBalance();
//
//        Transfer transfer1 = Transfer.builder()
//                .inAccCode("04-77825-0567386")
//                .inBankCode(BankCode.C04)
//                .outAccCode("04-23952-3659537")
//                .outBankCode(BankCode.C04)
//                .amount(100L)
//                .build();
//
//        Transfer transfer2 = Transfer.builder()
//                .inAccCode("04-77825-0567386")
//                .inBankCode(BankCode.C04)
//                .outAccCode("04-00706-3829355")
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
//                    accountService.transfer(transfer1, "soyeon", HisType.TRANSFER);
//                    accountService.transfer(transfer2, "sojisub", HisType.TRANSFER);
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//        latch.await();
//
//        // then
//        ApiResponse<AccountResponseDto.GetBalance> updatedOutAcc1 = accountService.getBalance("04-23952-3659537", "soyeon");
//        Long updatedOutAcc1Balance = updatedOutAcc1.getData().getAccBalance();
//        ApiResponse<AccountResponseDto.GetBalance> updatedOutAcc2 = accountService.getBalance("04-00706-3829355", "sojisub");
//        Long updatedOutAcc2Balance = updatedOutAcc2.getData().getAccBalance();
//        ApiResponse<AccountResponseDto.GetBalance> updatedInAcc = accountService.getBalance("04-77825-0567386", "samsiclover");
//        Long updatedInAccBalance = updatedInAcc.getData().getAccBalance();
//
//        assertThat(updatedOutAcc1Balance).isEqualTo(outAcc1Balance - 100L * numberOfThreads);
//        assertThat(updatedOutAcc2Balance).isEqualTo(outAcc2Balance - 100L * numberOfThreads);
//        assertThat(updatedInAccBalance).isEqualTo(inAccBalance + 200L * numberOfThreads);
//
//        log.info("이체 전 출금 계좌 1 잔액 = {}", outAcc1Balance);
//        log.info("이체 전 출금 계좌 2 잔액 = {}", outAcc2Balance);
//        log.info("이체 전 입금 계좌 잔액 = {}", inAccBalance);
//        log.info("-----계좌 이체 진행-----");
//        log.info("이체 후 출금 계좌 1 잔액 = {}", updatedOutAcc1Balance);
//        log.info("이체 후 출금 계좌 2 잔액 = {}", updatedOutAcc2Balance);
//        log.info("이체 후 입금 계좌 잔액 = {}", updatedInAccBalance);
//    }
}
