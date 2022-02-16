package com.sms.courier.generator;

import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.generator.annotation.CodeGenerator;
import com.sms.courier.utils.ExceptionUtils;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class CodeGeneratorContext implements ApplicationContextAware {

    private final ApplicationContext applicationContext;

    public CodeGeneratorContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private static final Map<Integer, Class<GeneratorStrategy>> GENERATOR_STRATEGY_BEAN_MAP = new HashMap<>();


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> orderStrategyMap = applicationContext.getBeansWithAnnotation(CodeGenerator.class);
        orderStrategyMap.forEach((k, v) -> {
            Class<GeneratorStrategy> generatorStrategyClass = (Class<GeneratorStrategy>) v.getClass();
            int type = generatorStrategyClass.getAnnotation(CodeGenerator.class).type().getCode();
            CodeGeneratorContext.GENERATOR_STRATEGY_BEAN_MAP.put(type, generatorStrategyClass);
        });
    }

    public GeneratorStrategy getGeneratorStrategy(Integer type) {
        Class<GeneratorStrategy> strategyClass = GENERATOR_STRATEGY_BEAN_MAP.get(type);
        if (strategyClass == null) {
            throw ExceptionUtils.mpe(ErrorCode.CODE_TYPE_IS_NOT_EXIST);
        }
        return applicationContext.getBean(strategyClass);
    }
}
