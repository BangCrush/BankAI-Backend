package com.hana.bankai.user.register;

import com.hana.bankai.domain.user.dto.UserRequestDto;
import com.hana.bankai.domain.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class RegisterCheckTest {

    @Autowired
    UserService service;

    @Test
    void contextLoads() {
        log.info("*** RegisterCheckTest ***");

        service.registerCheck(new UserRequestDto.RegisterCheck("990202-2111111")).toString();
        // 출력 안 됌
    }
}
