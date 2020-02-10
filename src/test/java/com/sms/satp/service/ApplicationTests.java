package com.sms.satp.service;

import com.sms.satp.SatpApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;


@SpringBootApplication(
    scanBasePackages = {
        "com.sms.satp.service",
        "com.sms.satp.mapper",
        "com.sms.satp.repository",
        "com.sms.satp.parser",
        "com.sms.satp.controller",
        "com.sms.satp.http",
        "com.sms.satp.engine"
    },
    exclude = {MongoAutoConfiguration.class, EmbeddedMongoAutoConfiguration.class}
)
public class ApplicationTests {

    public static void main(String[] args) {
        SpringApplication.run(SatpApplication.class, args);
    }
}
