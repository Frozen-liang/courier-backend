package com.sms.courier.initialize;

import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;

public interface DataInitializer extends Ordered {

    void init(ApplicationContext applicationContext);

}
