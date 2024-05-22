package com.hana.bankai.domain.account.entity;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.security.SecureRandom;

public class AccCodeGenerator implements IdentifierGenerator {

    private static final SecureRandom random = new SecureRandom();

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        return generateAccCode();
    }

    private String generateAccCode() {
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
