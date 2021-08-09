package com.sms.courier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoAuditing
@EnableAspectJAutoProxy
@EnableAsync
@EnableScheduling
public class CourierBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourierBackendApplication.class, args);
    }

}
