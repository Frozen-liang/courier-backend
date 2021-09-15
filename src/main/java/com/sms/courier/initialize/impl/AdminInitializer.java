package com.sms.courier.initialize.impl;

import static com.sms.courier.initialize.constant.Initializer.ADMIN_GROUP_NAME;
import static com.sms.courier.initialize.constant.Initializer.ADMIN_ID;
import static com.sms.courier.initialize.constant.Initializer.ADMIN_ROLE_ID;
import static com.sms.courier.initialize.constant.Initializer.GROUP_ID;

import com.sms.courier.entity.system.UserEntity;
import com.sms.courier.entity.system.UserGroupEntity;
import com.sms.courier.initialize.AdminProperties;
import com.sms.courier.initialize.DataInitializer;
import com.sms.courier.initialize.constant.Order;
import com.sms.courier.repository.UserGroupRepository;
import com.sms.courier.repository.UserRepository;
import com.sms.courier.utils.PasswordUtil;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AdminInitializer implements DataInitializer {

    private final AdminProperties adminProperties;

    public AdminInitializer(AdminProperties adminProperties) {
        this.adminProperties = adminProperties;
    }

    @Override
    public void init(ApplicationContext applicationContext) {
        try {
            UserRepository userRepository = applicationContext.getBean(UserRepository.class);
            UserGroupRepository userGroupRepository = applicationContext.getBean(UserGroupRepository.class);
            if (!userGroupRepository.existsById(GROUP_ID)) {
                log.info("Initialize group Admin.");
                UserGroupEntity userGroup = UserGroupEntity.builder().id(GROUP_ID).name(ADMIN_GROUP_NAME)
                    .roleIds(List.of(ADMIN_ROLE_ID))
                    .createDateTime(LocalDateTime.now())
                    .build();
                userGroupRepository.save(userGroup);
            }
            if (!userRepository.existsById(ADMIN_ID)) {
                PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
                String password = adminProperties.getPassword();
                if (StringUtils.isBlank(password)) {
                    password = PasswordUtil.randomPassword();
                    log.info("Admin password:{}", password);
                }
                UserEntity user = UserEntity.builder().id(ADMIN_ID).username(adminProperties.getUsername())
                    .groupId(GROUP_ID)
                    .password(passwordEncoder.encode(password))
                    .createDateTime(LocalDateTime.now())
                    .build();
                userRepository.save(user);
                log.info("Initialize Admin success.");
            }
        } catch (Exception e) {
            log.error("Initialize Admin error!", e);
        }

    }

    @Override
    public int getOrder() {
        return Order.ADMIN;
    }
}
