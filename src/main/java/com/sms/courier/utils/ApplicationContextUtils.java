package com.sms.courier.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtils {

    public static final ApplicationContextUtils INSTANCE = new ApplicationContextUtils();
    private  ApplicationContext applicationContext;

    private ApplicationContextUtils() {
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
