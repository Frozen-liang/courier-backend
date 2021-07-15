package com.sms.satp.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.common.enums.DocumentType;
import com.sms.satp.common.enums.SaveMode;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.api.ApiHistoryEntity;
import com.sms.satp.entity.group.ApiGroupEntity;
import com.sms.satp.entity.project.ImportSourceVo;
import com.sms.satp.entity.project.ProjectImportFlowEntity;
import com.sms.satp.mapper.ApiHistoryMapper;
import com.sms.satp.mapper.ApiHistoryMapperImpl;
import com.sms.satp.mapper.ProjectImportFlowMapper;
import com.sms.satp.repository.ApiGroupRepository;
import com.sms.satp.repository.ApiHistoryRepository;
import com.sms.satp.repository.ApiRepository;
import com.sms.satp.repository.ProjectImportFlowRepository;
import com.sms.satp.service.impl.AsyncServiceImpl;
import com.sms.satp.websocket.Payload;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.util.Streamable;

@DisplayName("Test for AsyncService")
public class AsyncServiceTest {

    private final ApiRepository apiRepository = mock(ApiRepository.class);
    private final ApiHistoryRepository apiHistoryRepository = mock(ApiHistoryRepository.class);
    private final ApiHistoryMapper apiHistoryMapper = new ApiHistoryMapperImpl();
    private final ApiGroupRepository apiGroupRepository = mock(ApiGroupRepository.class);
    private final ProjectImportFlowRepository projectImportFlowRepository = mock(ProjectImportFlowRepository.class);
    private final ProjectImportFlowMapper projectImportFlowMapper = mock(ProjectImportFlowMapper.class);
    private ApplicationContext applicationContext = mock(ApplicationContext.class);
    private final ApplicationEventPublisher applicationEventPublisher = mock(ApplicationEventPublisher.class);
    private final MessageService messageService = mock(MessageService.class);
    private final AsyncService asyncService = new AsyncServiceImpl(apiRepository, apiHistoryRepository,
        apiHistoryMapper, apiGroupRepository, projectImportFlowRepository, projectImportFlowMapper,
        applicationEventPublisher,
        messageService);

    @Test
    @DisplayName("Test the importApi method in the async service")
    public void importApi_increment_test() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("config/swagger_java.json");
        String source = IOUtils.toString(classPathResource.getInputStream(), StandardCharsets.UTF_8);
        ImportSourceVo importSource = ImportSourceVo.builder().source(source).projectId(ObjectId.get().toString())
            .saveMode(SaveMode.INCREMENT).documentType(
                DocumentType.SWAGGER_FILE).build();
        run();
        asyncService.importApi(importSource);
        verify(apiRepository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("Test the importApi method in the async service")
    public void importApi_cover_test() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("config/swagger_java.json");
        String source = IOUtils.toString(classPathResource.getInputStream(), StandardCharsets.UTF_8);
        ImportSourceVo importSource = ImportSourceVo.builder().source(source).projectId(ObjectId.get().toString())
            .saveMode(SaveMode.COVER).documentType(
                DocumentType.SWAGGER_FILE).build();

        run();
        asyncService.importApi(importSource);
        verify(apiRepository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("Test the importApi method in the async service")
    public void importApi_remain_test() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("config/swagger_java.json");
        String source = IOUtils.toString(classPathResource.getInputStream(), StandardCharsets.UTF_8);
        ImportSourceVo importSource = ImportSourceVo.builder().source(source).projectId(ObjectId.get().toString())
            .saveMode(SaveMode.REMAIN).documentType(
                DocumentType.SWAGGER_FILE).build();
        run();
        asyncService.importApi(importSource);
        verify(apiRepository, times(1)).saveAll(any());
    }

    private void run() {
        when(projectImportFlowRepository.save(any(ProjectImportFlowEntity.class))).thenReturn(
            ProjectImportFlowEntity.builder().build());
        doNothing().when(messageService).projectMessage(any(), any(Payload.class));
        when(apiGroupRepository.findApiGroupEntitiesByProjectId(any()))
            .thenReturn(getApiGroup());
        when(apiRepository.findApiEntitiesByProjectIdAndSwaggerIdNotNull(any()))
            .thenReturn(Streamable.of(getApi()));
        when(apiRepository.saveAll(any())).thenReturn(getApi());
        when(applicationContext.getBean(ProjectImportFlowRepository.class)).thenReturn(projectImportFlowRepository);
        when(applicationContext.getBean(MessageService.class)).thenReturn(messageService);
        doNothing().when(apiRepository).deleteAll();
        doNothing().when(applicationEventPublisher).publishEvent(any());
        when(apiRepository.saveAll(any())).thenReturn(getApi());
        when(apiHistoryRepository.insert(any(ApiHistoryEntity.class))).thenReturn(null);
    }

    private List<ApiGroupEntity> getApiGroup() {
        ArrayList<ApiGroupEntity> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(ApiGroupEntity.builder().id(ObjectId.get().toString()).name("test" + Math.random()).build());
        }
        return list;
    }

    private List<ApiEntity> getApi() {
        ArrayList<ApiEntity> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(ApiEntity.builder().id(ObjectId.get().toString()).swaggerId("swagger" + Math.random())
                .apiName("test" + Math.random()).build());
        }
        return list;
    }

}
