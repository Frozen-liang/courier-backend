package com.sms.satp.initialize;

import com.sms.satp.entity.system.UserGroupEntity;
import com.sms.satp.repository.UserGroupRepository;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

public class DefaultUserGroupInitializer implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        UserGroupRepository userGroupRepository = applicationContext.getBean(UserGroupRepository.class);
        boolean exists = userGroupRepository.existsByDefaultGroupIsTrue();
        if (exists) {
            return;
        }
        UserGroupEntity userGroup = UserGroupEntity.builder().name("默认分组").defaultGroup(true).build();
        userGroupRepository.save(userGroup);
    }
}
