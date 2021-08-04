package com.sms.courier.initialize.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.courier.entity.system.UserGroupEntity;
import com.sms.courier.repository.UserGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;

@DisplayName("Tests for DefaultUserGroupInitializerTest")
public class DefaultUserGroupInitializerTest {

    private final DefaultUserGroupInitializer defaultUserGroupInitializer = new DefaultUserGroupInitializer();
    private final ConfigurableApplicationContext applicationContext = mock(ConfigurableApplicationContext.class);
    private final UserGroupRepository userGroupRepository = mock(UserGroupRepository.class);
    private final UserGroupEntity userGroupEntity = UserGroupEntity.builder().name("默认分组").defaultGroup(true).build();


    @Test
    @DisplayName("Test the init method in the DefaultUserGroupInitializer")
    public void init_not_exist_test() {
        when(applicationContext.getBean(UserGroupRepository.class)).thenReturn(userGroupRepository);
        when(userGroupRepository.existsByDefaultGroupIsTrue()).thenReturn(false);
        defaultUserGroupInitializer.init(applicationContext);
        verify(userGroupRepository).save(userGroupEntity);
    }

    @Test
    @DisplayName("Test the init method in the DefaultUserGroupInitializer")
    public void init_exist_test() {
        when(applicationContext.getBean(UserGroupRepository.class)).thenReturn(userGroupRepository);
        when(userGroupRepository.existsByDefaultGroupIsTrue()).thenReturn(true);
        verify(userGroupRepository, never()).save(userGroupEntity);
    }
}
