package com.hana.bankai.domain.account.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@RequiredArgsConstructor
@Component
public class AccCodeGenerator{

    private static final SecureRandom random = new SecureRandom();

    public String generateAccCode() {
        StringBuilder accCode = new StringBuilder(16);
        accCode.append("04-");
        for (int i = 0; i < 5; i ++) {
            accCode.append(random.nextInt(10));
        }
        accCode.append("-");
        for (int i = 0; i < 7; i ++) {
            accCode.append(random.nextInt(10));
        }

        return accCode.toString();
    }
}
