package com.sms.courier.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AsyncConfig {

    @Bean("commonExecutor")
    public Executor commonExecutor() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("common-thread-%d").build();
        return new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(500), threadFactory);
    }

    @Bean("importApiExecutor")
    public Executor importApiExecutor() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("import-api-thread-%d").build();
        return new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100), threadFactory);
    }

}
