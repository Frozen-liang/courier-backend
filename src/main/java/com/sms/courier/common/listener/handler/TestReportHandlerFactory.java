package com.sms.courier.common.listener.handler;


import com.sms.courier.common.listener.handler.enums.TestReportType;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class TestReportHandlerFactory implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;
    private Map<TestReportType, TestReportHandler> testReportHandlerMap;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Object> beans = applicationContext
            .getBeansWithAnnotation(TestReportHandleType.class);
        testReportHandlerMap = beans.values().stream().map(TestReportHandler.class::cast)
            .collect(Collectors
                .toMap((type) -> AnnotationUtils.findAnnotation(type.getClass(), TestReportHandleType.class).type(),
                    Function.identity()));
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public TestReportHandler getHandler(TestReportType testReportType) {
        if (!testReportHandlerMap.containsKey(testReportType)) {
            throw new IllegalArgumentException(String.format("The test report type [%s] is not supported.",
                testReportType));
        }
        return testReportHandlerMap.get(testReportType);
    }

}
