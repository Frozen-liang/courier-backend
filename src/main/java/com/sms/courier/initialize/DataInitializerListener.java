package com.sms.courier.initialize;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.OrderComparator;

public class DataInitializerListener implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        Collection<DataInitializer> dataInitializers = applicationContext
            .getBeansOfType(DataInitializer.class).values();
        List<DataInitializer> dataInitializerList = new ArrayList<>(
            Objects.requireNonNullElse(dataInitializers, Collections.emptyList())
        );
        OrderComparator.sort(dataInitializerList);
        dataInitializerList.forEach(dataInitializer -> dataInitializer.init(applicationContext));
    }
}
