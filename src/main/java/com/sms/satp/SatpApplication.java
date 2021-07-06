package com.sms.satp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableMongoAuditing
@EnableAspectJAutoProxy
@EnableAsync
public class SatpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SatpApplication.class, args);
    }

}
