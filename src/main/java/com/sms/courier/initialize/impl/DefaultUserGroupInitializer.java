package com.sms.courier.initialize.impl;

import com.sms.courier.entity.system.UserGroupEntity;
import com.sms.courier.initialize.DataInitializer;
import com.sms.courier.repository.UserGroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultUserGroupInitializer implements DataInitializer {

    @Override
    public void init(ApplicationContext applicationContext) {
        UserGroupRepository userGroupRepository = applicationContext.getBean(UserGroupRepository.class);
        boolean exists = userGroupRepository.existsByDefaultGroupIsTrue();
        if (exists) {
            return;
        }
        UserGroupEntity userGroup = UserGroupEntity.builder().name("默认分组").defaultGroup(true).build();
        userGroupRepository.save(userGroup);
        log.debug("Init DefaultUserGroup.");
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
