package com.hana.bankai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static io.lettuce.core.pubsub.PubSubOutput.Type.message;

@EnableJpaAuditing
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableJpaAuditing
public class BankaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankaiApplication.class, args);
    }

}
