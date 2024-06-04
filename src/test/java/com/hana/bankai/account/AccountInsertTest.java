package com.hana.bankai.account;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.security.SecureRandom;

@Slf4j
@SpringBootTest
class AccountInsertTest {

    private static final SecureRandom random = new SecureRandom();

    private String generateTestAccCode() {
        StringBuilder accCode = new StringBuilder(16);
        accCode.append("04-");
        for (int i = 0; i < 5; i++) {
            accCode.append(random.nextInt(10));
        }
        accCode.append("-");
        for (int i = 0; i < 7; i++) {
            accCode.append(random.nextInt(10));
        }
        return accCode.toString();
    }

//    @Test
//    void 계좌생성테스트() {
//        // 테스트 계정 코드 생성
//        String accCode = generateTestAccCode();
//        // 로그에 출력하여 생성된 코드를 확인
//        log.info("Generated Account Code: {}", accCode);
//    }
}
