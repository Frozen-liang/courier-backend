package com.sms.courier.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EnvUtils implements EnvironmentAware {

    private static Integer PORT;

    private EnvUtils() {
    }

    @Override
    public void setEnvironment(Environment environment) {
        PORT = environment.getProperty("server.port", Integer.class);
    }

    public static Integer getPort() {
        return PORT;
    }
}
