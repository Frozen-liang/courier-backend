package com.sms.courier.initialize.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.courier.entity.system.SystemVersionEntity;
import com.sms.courier.repository.SystemVersionRepository;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

@DisplayName("Tests for RoleInitializerTest")
public class DataInitializerImplTest {

    private ConfigurableApplicationContext applicationContext = mock(ConfigurableApplicationContext.class);
    private final SystemVersionRepository systemVersionRepository = mock(SystemVersionRepository.class);
    private final BuildProperties buildProperties = mock(BuildProperties.class);
    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Instant instant = Instant.now();
    private final SystemVersionEntity systemVersionEntity = SystemVersionEntity.builder().id(ID)
        .version(VERSION).name(NAME).group(GROUP).buildTime(LOCAL_DATE_TIME).initialized(true).status(0)
        .createDateTime(LOCAL_DATE_TIME).modifyDateTime(LOCAL_DATE_TIME).build();
    private final DataInitializerImpl dataInitializerImpl = new DataInitializerImpl();
    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();
    private static final String ID = ObjectId.get().toString();
    private static final String NAME = "test";
    private static final String VERSION = "1.0.0";
    private static final String GROUP = "test";

    @Test
    @DisplayName("Test the onApplicationEvent_test method in the DataInitializerImpl")
    public void onApplicationEvent_test() throws IOException {
        when(applicationContext.getBean(BuildProperties.class)).thenReturn(buildProperties);
        when(applicationContext.getBean(ObjectMapper.class)).thenReturn(objectMapper);
        when(applicationContext.getBean(MongoTemplate.class)).thenReturn(mongoTemplate);
        when(applicationContext.getBean(SystemVersionRepository.class)).thenReturn(systemVersionRepository);
        when(buildProperties.getTime()).thenReturn(instant);
        when(buildProperties.getVersion()).thenReturn(VERSION);
        when(buildProperties.getName()).thenReturn(NAME);
        when(buildProperties.getGroup()).thenReturn(GROUP);
        when(systemVersionRepository.findByVersion(VERSION)).thenReturn(systemVersionEntity);
        dataInitializerImpl.init(applicationContext);
        verify(mongoTemplate, times(1)).insert(any(), any(Class.class));
        verify(systemVersionRepository, times(1)).save(systemVersionEntity);
    }

    @Test
    @DisplayName("Test the onApplicationEvent_test method in the DataInitializerImpl")
    public void onApplicationEvent_path_not_exist_test() {
        when(applicationContext.getBean(BuildProperties.class)).thenReturn(buildProperties);
        when(applicationContext.getBean(MongoTemplate.class)).thenReturn(mongoTemplate);
        when(applicationContext.getBean(ObjectMapper.class)).thenReturn(objectMapper);
        when(applicationContext.getBean(SystemVersionRepository.class)).thenReturn(systemVersionRepository);
        when(buildProperties.getTime()).thenReturn(instant);
        when(buildProperties.getVersion()).thenReturn("v1");
        when(buildProperties.getName()).thenReturn(NAME);
        when(buildProperties.getGroup()).thenReturn(GROUP);
        when(systemVersionRepository.findByVersion("v1")).thenReturn(systemVersionEntity);
        dataInitializerImpl.init(applicationContext);
        verify(mongoTemplate, never()).insert(any(), any(Class.class));
        verify(systemVersionRepository, times(1)).save(systemVersionEntity);
    }
}
