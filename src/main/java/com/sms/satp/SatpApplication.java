package com.sms.satp;

import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.repository.ApiRepository;
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
    ApiRepository apiRepository;

    public static void main(String[] args) {
        SpringApplication.run(SatpApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        ApiEntity apiEntity = ApiEntity.builder().build();
        apiEntity.setId("60813a92819ee37e453a4918");
        apiRepository.save(apiEntity);
    }
}
