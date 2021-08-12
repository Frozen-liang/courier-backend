package com.sms.courier.initialize.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.courier.repository.UserGroupRepository;
import com.sms.courier.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;

@DisplayName("Tests for AdminInitializerTest")
public class AdminInitializerTest {

    private final AdminInitializer adminInitializer = new AdminInitializer();
    private final ConfigurableApplicationContext applicationContext = mock(ConfigurableApplicationContext.class);
    private final UserGroupRepository userGroupRepository = mock(UserGroupRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private static final String USER_ID = "6110d05508d2cf752483a7f9";
    private static final String GROUP_ID = "6110d05508d2cf752483a7fa";

    @Test
    @DisplayName("Test the init method in the AdminInitializer")
    public void init_not_exist_test() {
        when(applicationContext.getBean(UserGroupRepository.class)).thenReturn(userGroupRepository);
        when(applicationContext.getBean(UserRepository.class)).thenReturn(userRepository);
        when(userGroupRepository.existsById(GROUP_ID)).thenReturn(false);
        when(userRepository.existsById(USER_ID)).thenReturn(false);
        adminInitializer.init(applicationContext);
        verify(userGroupRepository).save(any());
        verify(userRepository).save(any());
    }

    @Test
    @DisplayName("Test the init method in the AdminInitializer")
    public void init_exist_test() {
        when(applicationContext.getBean(UserGroupRepository.class)).thenReturn(userGroupRepository);
        when(applicationContext.getBean(UserRepository.class)).thenReturn(userRepository);
        when(userGroupRepository.existsById(GROUP_ID)).thenReturn(true);
        when(userRepository.existsById(USER_ID)).thenReturn(true);
        adminInitializer.init(applicationContext);
        verify(userGroupRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

}
