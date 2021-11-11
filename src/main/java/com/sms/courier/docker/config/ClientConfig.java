package com.sms.courier.docker.config;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.EventType;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient.Builder;
import com.sms.courier.common.enums.ContainerStatus;
import com.sms.courier.docker.enmu.LabelType;
import com.sms.courier.docker.event.ContainerEvent;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {


    @Bean
    public DockerClient dockerClient(ContainerEvent event) {
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        DockerClient client = DockerClientBuilder.getInstance().withDockerHttpClient(
            new Builder().dockerHost(config.getDockerHost()).sslConfig(config.getSSLConfig())
                .build()).build();
        client.eventsCmd().withEventTypeFilter(EventType.CONTAINER)
            .withLabelFilter(LabelType.getLabels())
            .withEventFilter(
                Arrays.stream(ContainerStatus.values()).map(ContainerStatus::getName).toArray(String[]::new)
            )
            .exec(event);
        return client;
    }

}
