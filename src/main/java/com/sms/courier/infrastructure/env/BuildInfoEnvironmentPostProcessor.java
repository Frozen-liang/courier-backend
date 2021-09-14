package com.sms.courier.infrastructure.env;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class BuildInfoEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    public static final String COURIER_BUILD = "courier.build.";
    public static final String BUILD_KEY = "build.";
    public static final int ORDER = ConfigDataEnvironmentPostProcessor.ORDER + 10;
    private static final String BUILD_INFO_SOURCE_NAME = "courierEnvironment";
    private static final Resource BUILD_INFO_LOCATION = new ClassPathResource("META-INF/build-info.properties");

    @SneakyThrows
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (BUILD_INFO_LOCATION.exists()) {
            Properties properties = loadFrom();
            OriginTrackedMapPropertySource originTrackedMapPropertySource = new OriginTrackedMapPropertySource(
                BUILD_INFO_SOURCE_NAME, properties);
            environment.getPropertySources().addLast(originTrackedMapPropertySource);
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    protected Properties loadFrom() throws IOException {
        Properties source = loadSource();
        Properties target = new Properties();
        for (String key : source.stringPropertyNames()) {
            if (key.startsWith(BUILD_KEY)) {
                target.put(key.replace(BUILD_KEY, COURIER_BUILD), source.get(key));
            }
        }
        return target;
    }

    private Properties loadSource() throws IOException {
        return PropertiesLoaderUtils.loadProperties(new EncodedResource(
            BuildInfoEnvironmentPostProcessor.BUILD_INFO_LOCATION, StandardCharsets.UTF_8));
    }
}
