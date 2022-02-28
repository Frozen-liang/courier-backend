package com.sms.courier.storagestrategy.strategy.impl;

import com.sms.courier.storagestrategy.StorageType;
import com.sms.courier.storagestrategy.strategy.FileStorageService;
import com.sms.courier.storagestrategy.strategy.StorageStrategy;
import com.sms.courier.storagestrategy.strategy.StorageStrategyFactory;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

@Component
public class StorageStrategyFactoryImpl implements StorageStrategyFactory, InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private Map<StorageType, FileStorageService> storageStrategyMap;

    @Override
    public FileStorageService fetchStorageStrategy() {
        FileStorageService amazon = storageStrategyMap.get(StorageType.AMAZON);
        return amazon.isEnable() ? amazon : storageStrategyMap.get(StorageType.MONGO);
    }

    @Override
    public FileStorageService fetchStorageStrategy(StorageType type) {
        return storageStrategyMap.get(type);
    }

    @Override
    public void afterPropertiesSet() {
        Map<String, Object> beans = this.applicationContext
            .getBeansWithAnnotation(StorageStrategy.class);
        this.storageStrategyMap = beans.values().stream()
            .map(FileStorageService.class::cast)
            .collect(Collectors.toMap(strategy -> AnnotationUtils
                .findAnnotation(strategy.getClass(), StorageStrategy.class).type(), Function.identity()));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
