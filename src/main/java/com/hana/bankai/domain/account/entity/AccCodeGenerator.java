package com.hana.bankai.domain.account.entity;

import com.hana.bankai.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.stereotype.Component;

import java.io.Serializable;
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
