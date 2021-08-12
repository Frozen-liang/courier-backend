package com.sms.courier.initialize.impl;

import com.sms.courier.entity.system.UserEntity;
import com.sms.courier.entity.system.UserGroupEntity;
import com.sms.courier.initialize.DataInitializer;
import com.sms.courier.repository.UserGroupRepository;
import com.sms.courier.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AdminInitializer implements DataInitializer {

    private static final String ADMIN = "Admin";
    private static final String USER_ID = "6110d05508d2cf752483a7f9";
    private static final String GROUP_ID = "6110d05508d2cf752483a7fa";
    private static final String ADMIN_ROLE_ID = "610126d056617f032835a951";
    private static final String PASSWORD = "$2a$10$6oP2CBWdiSFGwl6QUL70r..exYTiFDjnbsA4c6jpE70WmORG5GIN.";
    private static final String EMAIL = ADMIN + "@starlight-sms.com";

    @Override
    public void init(ApplicationContext applicationContext) {
        UserRepository userRepository = applicationContext.getBean(UserRepository.class);
        UserGroupRepository userGroupRepository = applicationContext.getBean(UserGroupRepository.class);
        if (!userGroupRepository.existsById(GROUP_ID)) {
            log.debug("Initialize group Admin.");
            UserGroupEntity userGroup = UserGroupEntity.builder().id(GROUP_ID).name(ADMIN)
                .roleIds(List.of(ADMIN_ROLE_ID))
                .createDateTime(LocalDateTime.now())
                .build();
            userGroupRepository.save(userGroup);
        }
        if (!userRepository.existsById(USER_ID)) {
            log.debug("Initialize user Admin .");
            UserEntity user = UserEntity.builder().id(USER_ID).nickname(ADMIN).email(EMAIL).username(ADMIN)
                .groupId(GROUP_ID).password(PASSWORD).createDateTime(LocalDateTime.now()).build();
            userRepository.save(user);
        }

    }

    @Override
    public int getOrder() {
        return 3;
    }
}
