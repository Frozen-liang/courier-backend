package com.sms.courier.initialize.impl;

import static com.sms.courier.initialize.constant.Initializer.DEFAULT_GROUP_NAME;

import com.sms.courier.entity.system.UserGroupEntity;
import com.sms.courier.initialize.DataInitializer;
import com.sms.courier.initialize.constant.Order;
import com.sms.courier.repository.UserGroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultUserGroupInitializer implements DataInitializer {

    @Override
    public void init(ApplicationContext applicationContext) {
        try {
            UserGroupRepository userGroupRepository = applicationContext.getBean(UserGroupRepository.class);
            boolean exists = userGroupRepository.existsByDefaultGroupIsTrue();
            if (exists) {
                return;
            }
            UserGroupEntity userGroup = UserGroupEntity.builder().name(DEFAULT_GROUP_NAME).defaultGroup(true).build();
            userGroupRepository.save(userGroup);
            log.info("Init DefaultUserGroup.");
        } catch (BeansException e) {
            log.error("Init DefaultUserGroup error.", e);
        }
    }

    @Override
    public int getOrder() {
        return Order.DEFAULT_USER_GROUP;
    }
}
