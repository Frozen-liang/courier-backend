package com.sms.satp.initialize;

import com.sms.satp.entity.system.UserGroupEntity;
import com.sms.satp.repository.UserGroupRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserGroupInitialize implements InitializingBean {

    private final UserGroupRepository userGroupRepository;

    public DefaultUserGroupInitialize(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        boolean exists = userGroupRepository.existsByDefaultGroupIsTrue();
        if (exists) {
            return;
        }
        UserGroupEntity userGroup = UserGroupEntity.builder().name("默认分组").defaultGroup(true).build();
        userGroupRepository.save(userGroup);
    }
}
