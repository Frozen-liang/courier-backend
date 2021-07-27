package com.sms.satp.initialize;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.entity.system.UserGroupEntity;
import com.sms.satp.repository.UserGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ConfigurableApplicationContext;

@DisplayName("Tests for DefaultUserGroupInitializerTest")
public class DefaultUserGroupInitializerTest {

    private final DefaultUserGroupInitializer defaultUserGroupInitializer = new DefaultUserGroupInitializer();
    private ConfigurableApplicationContext applicationContext = mock(ConfigurableApplicationContext.class);
    private final ApplicationStartedEvent applicationStartedEvent = mock(ApplicationStartedEvent.class);
    private final UserGroupRepository userGroupRepository = mock(UserGroupRepository.class);
    private final UserGroupEntity userGroupEntity = UserGroupEntity.builder().name("默认分组").defaultGroup(true).build();


    @Test
    @DisplayName("Test the onApplicationEvent_test method in the DefaultUserGroupInitializer")
    public void onApplicationEvent_not_exist_test(){
        when(applicationStartedEvent.getApplicationContext()).thenReturn(applicationContext);
        when(applicationContext.getBean(UserGroupRepository.class)).thenReturn(userGroupRepository);
        defaultUserGroupInitializer.onApplicationEvent(applicationStartedEvent);
        verify(userGroupRepository).save(userGroupEntity);
    }

    @Test
    @DisplayName("Test the onApplicationEvent_test method in the DefaultUserGroupInitializer")
    public void onApplicationEvent_exist_test(){
        when(applicationStartedEvent.getApplicationContext()).thenReturn(applicationContext);
        when(applicationContext.getBean(UserGroupRepository.class)).thenReturn(userGroupRepository);
        when(userGroupRepository.existsByDefaultGroupIsTrue()).thenReturn(true);
        defaultUserGroupInitializer.onApplicationEvent(applicationStartedEvent);
        verify(userGroupRepository,never()).save(userGroupEntity);
    }
}
