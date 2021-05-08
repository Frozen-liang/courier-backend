package com.sms.satp;

import com.sms.satp.dto.ApiImportRequest;
import com.sms.satp.parser.common.DocumentType;
import com.sms.satp.service.ApiService;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableAdminServer
@SpringBootApplication
@EnableMongoAuditing
public class SatpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SatpApplication.class, args);
    }


}
