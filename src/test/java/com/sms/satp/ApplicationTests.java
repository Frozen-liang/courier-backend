package com.sms.satp;

import com.sms.satp.SatpApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;


@TestConfiguration
@EnableAutoConfiguration(exclude = {EmbeddedMongoAutoConfiguration.class})
public class ApplicationTests {

    public static void main(String[] args) {
        SpringApplication.run(SatpApplication.class, args);
    }
}
