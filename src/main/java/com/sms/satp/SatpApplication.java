package com.sms.satp;

import com.sms.satp.dto.ApiImportRequest;
import com.sms.satp.parser.common.DocumentType;
import com.sms.satp.service.ApiService;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableAdminServer
@SpringBootApplication
@EnableMongoAuditing
public class SatpApplication implements CommandLineRunner {

    @Autowired
    private ApiService apiService;

    public static void main(String[] args) {
        SpringApplication.run(SatpApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ApiImportRequest apiImportRequest = ApiImportRequest.builder().documentType(DocumentType.SWAGGER.getType())
            .documentUrl("https://petstore.swagger"
                + ".io/v2/swagger.json").build();
        apiService.importDocument(apiImportRequest);
    }
}
