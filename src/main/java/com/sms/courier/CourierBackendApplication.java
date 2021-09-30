package com.sms.courier;

import com.sms.courier.utils.IgnoreMongoCheck;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {MailSenderAutoConfiguration.class})
@EnableMongoAuditing
@EnableAspectJAutoProxy
@EnableAsync
@EnableScheduling
public class CourierBackendApplication {

    static {
        // Temporarily fix MongoDB check $ /. Problem
        IgnoreMongoCheck.run();
    }

    public static void main(String[] args) {
        SpringApplication.run(CourierBackendApplication.class, args);
    }

}
