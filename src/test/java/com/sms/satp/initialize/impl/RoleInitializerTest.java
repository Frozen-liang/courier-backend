package com.sms.satp.initialize.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.satp.common.enums.RoleType;
import com.sms.satp.entity.system.SystemRoleEntity;
import com.sms.satp.entity.system.SystemVersionEntity;
import com.sms.satp.repository.SystemRoleRepository;
import com.sms.satp.repository.SystemVersionRepository;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.ConfigurableApplicationContext;

@DisplayName("Tests for RoleInitializerTest")
public class RoleInitializerTest {

    private ConfigurableApplicationContext applicationContext = mock(ConfigurableApplicationContext.class);
    private final SystemVersionRepository systemVersionRepository = mock(SystemVersionRepository.class);
    private final BuildProperties buildProperties = mock(BuildProperties.class);
    private final SystemRoleRepository systemRoleRepository = mock(SystemRoleRepository.class);
    private final ObjectMapper objectMapper = mock(ObjectMapper.class);

    private final Instant instant = Instant.now();

    private final SystemVersionEntity systemVersionEntity = SystemVersionEntity.builder().id(ID)
        .version(VERSION).name(NAME).group(GROUP).buildTime(LOCAL_DATE_TIME).initialized(true).status(0)
        .createDateTime(LOCAL_DATE_TIME).modifyDateTime(LOCAL_DATE_TIME).build();
    private final List<SystemRoleEntity> systemRoleEntities = List
        .of(SystemRoleEntity.builder().id(ID).name(NAME).description(NAME).roleType(
            RoleType.ENGINE).createDateTime(LOCAL_DATE_TIME).build());

    private final RoleConvert roleConvert = new RoleConvert(systemRoleEntities);
    private final RoleInitializer roleInitializer = new RoleInitializer();

    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();
    private static final String ID = ObjectId.get().toString();
    private static final String NAME = "test";
    private static final String VERSION = "1.0.0-SNAPSHOT";
    private static final String GROUP = "test";

    @Test
    @DisplayName("Test the onApplicationEvent_test method in the RoleInitializer")
    public void onApplicationEvent_test() throws IOException {
        when(applicationContext.getBean(BuildProperties.class)).thenReturn(buildProperties);
        when(applicationContext.getBean(SystemRoleRepository.class)).thenReturn(systemRoleRepository);
        when(applicationContext.getBean(SystemVersionRepository.class)).thenReturn(systemVersionRepository);
        when(applicationContext.getBean(ObjectMapper.class)).thenReturn(objectMapper);
        when(buildProperties.getTime()).thenReturn(instant);
        when(buildProperties.getVersion()).thenReturn(VERSION);
        when(buildProperties.getName()).thenReturn(NAME);
        when(buildProperties.getGroup()).thenReturn(GROUP);
        when(systemVersionRepository.findByName(NAME)).thenReturn(systemVersionEntity);
        when(objectMapper.readValue(any(InputStream.class), any(Class.class))).thenReturn(roleConvert);
        roleInitializer.init(applicationContext);
        verify(systemRoleRepository, times(1)).saveAll(systemRoleEntities);
        verify(systemVersionRepository, times(1)).save(systemVersionEntity);
    }

    @Test
    @DisplayName("Test the onApplicationEvent_test method in the RoleInitializer")
    public void onApplicationEvent_path_not_exist_test() {
        when(applicationContext.getBean(BuildProperties.class)).thenReturn(buildProperties);
        when(applicationContext.getBean(SystemRoleRepository.class)).thenReturn(systemRoleRepository);
        when(applicationContext.getBean(SystemVersionRepository.class)).thenReturn(systemVersionRepository);
        when(buildProperties.getTime()).thenReturn(instant);
        when(buildProperties.getVersion()).thenReturn("v1");
        when(buildProperties.getName()).thenReturn(NAME);
        when(buildProperties.getGroup()).thenReturn(GROUP);
        when(systemVersionRepository.findByName(NAME)).thenReturn(systemVersionEntity);
        roleInitializer.init(applicationContext);
        verify(systemVersionRepository).save(systemVersionEntity);
    }
}
