package com.sms.satp.security.strategy.impl;

import com.sms.satp.security.TokenType;
import com.sms.satp.security.strategy.SatpSecurityStrategy;
import com.sms.satp.security.strategy.SecurityStrategy;
import com.sms.satp.security.strategy.SecurityStrategyFactory;
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
public class SecurityStrategyFactoryImpl implements SecurityStrategyFactory, InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private Map<TokenType, SatpSecurityStrategy> securityStrategyMap;

    @Override
    public SatpSecurityStrategy fetchSecurityStrategy(TokenType tokenType) {
        if (!securityStrategyMap.containsKey(tokenType)) {
            throw new IllegalArgumentException(String.format("The token type [%s] is not supported.", tokenType));
        }
        return securityStrategyMap.get(tokenType);
    }

    @Override
    public void afterPropertiesSet() {
        Map<String, Object> beans = this.applicationContext
            .getBeansWithAnnotation(SecurityStrategy.class);
        this.securityStrategyMap = beans.values().stream()
            .map(SatpSecurityStrategy.class::cast)
            .collect(Collectors.toMap(strategy -> AnnotationUtils
                .findAnnotation(strategy.getClass(), SecurityStrategy.class).type(), Function.identity()));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}