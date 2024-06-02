package com.hana.bankai;

import com.hana.bankai.domain.product.entity.ProdType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@SpringBootTest
class BankaiApplicationTests {

    @Test
    void contextLoads() {
        log.info("RUN TEST PROGRAM");
    }

    @Test
    void test() {
        int code = 1;
        ProdType prodType = ProdType.of(code);
        log.info(prodType.toString());
    }
}
