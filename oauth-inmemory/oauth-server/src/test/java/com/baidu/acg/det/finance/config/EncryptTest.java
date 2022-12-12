package com.baidu.acg.det.finance.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

/**
 *
 */
@SpringBootTest
public class EncryptTest {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void encode(){
        String encode = passwordEncoder.encode("123");
        System.out.println(encode);
        // $2a$10$Pa0BsHDM7ChDkJNo8UCHd.8OXHkyZcohSevHUJ492ZSZZmF1eWmLW
    }
}
