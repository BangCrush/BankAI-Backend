package com.hana.bankai.user;

import com.hana.bankai.domain.user.dto.UserRequestDto;
import com.hana.bankai.domain.user.entity.UserJob;
import com.hana.bankai.domain.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class InsertTests {

    @Autowired
    UserService service;

    @Test
    void contextLoads() {
        log.info(" *** User Insert Test *** ");

        UserRequestDto.Register register = UserRequestDto.Register.builder()
                .userId("user02")
                .userPwd("1111")
                .userNameKr("홍길동")
                .userInherentNumber("901234-1234568")
                .userPhone("010-1234-1234")
                .userAddr("대한민국")
                .userAddrDetail("부산")
                .userNameEn("Hong")
                .userEmail("hong@gmail.com")
                .build();

        service.register(register);
    }

}

