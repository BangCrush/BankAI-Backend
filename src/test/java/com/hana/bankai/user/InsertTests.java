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

    /*
    @Test
    void contextLoads() {
        log.info(" *** User Insert Test *** ");

        UserRequestDto.Register register = UserRequestDto.Register.builder()
                .userId("user01")
                .userPwd("1111")
                .userNameKr("봉식이")
                .userInherentNumber("901234-1234570")
                .userPhone("010-1234-1236")
                .userAddr("대한민국")
                .userAddrDetail("대구")
                .userNameEn("Bong")
                .userEmail("bong@gmail.com")
                .build();

        service.register(register);
    }
    */

}

