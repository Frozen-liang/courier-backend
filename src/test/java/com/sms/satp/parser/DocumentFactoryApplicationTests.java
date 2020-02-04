package com.sms.satp.parser;

import com.sms.satp.SatpApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;


@SpringBootApplication(
    scanBasePackages = {
        "com.sms.satp.parser"
    }
    , exclude = {MongoAutoConfiguration.class, EmbeddedMongoAutoConfiguration.class}
)
public class DocumentFactoryApplicationTests {

    public static void main(String[] args) {
        SpringApplication.run(SatpApplication.class, args);
    }
}
