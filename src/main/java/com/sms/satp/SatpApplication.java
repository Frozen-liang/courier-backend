package com.sms.satp;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
@Slf4j
public class SatpApplication {


    public static void main(String[] args) {
        SpringApplication.run(SatpApplication.class, args);
    }


}
