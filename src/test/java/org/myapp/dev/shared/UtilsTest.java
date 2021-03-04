package org.myapp.dev.shared;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.myapp.dev.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class UtilsTest {

    @Autowired
    AppConfig appConfig;

    @Test
    void generateUserId() {

        assertTrue(appConfig.getH2consoleUrl().equalsIgnoreCase("/h2-console/**"));
    }

    @Test
    void generateAddressId() {
    }
}