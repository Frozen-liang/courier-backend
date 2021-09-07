package com.sms.courier.initialize.impl;

import com.sms.courier.entity.schedule.ServicesEntity;
import com.sms.courier.initialize.DataInitializer;
import com.sms.courier.repository.ServicesRepository;
import java.net.InetAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ServicesInitializer implements DataInitializer {

    @Value("${server.port}")
    private int port;

    @Override
    public void init(ApplicationContext applicationContext) {
        try {
            ServicesRepository servicesRepository = applicationContext.getBean(ServicesRepository.class);
            String ip = InetAddress.getLocalHost().getHostAddress();
            ServicesEntity servicesEntity = servicesRepository.findByIpAndPort(ip, port)
                .orElse(ServicesEntity.builder().ip(ip).port(port).build());
            servicesEntity.setAvailableServices(true);
            servicesRepository.save(servicesEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public int getOrder() {
        return 100;
    }
}
