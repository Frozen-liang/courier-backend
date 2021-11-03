package com.sms.courier.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.courier.common.enums.DocumentType;
import com.sms.courier.common.enums.SaveMode;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.group.ApiGroupEntity;
import com.sms.courier.entity.project.ImportSourceVo;
import com.sms.courier.entity.project.ProjectImportFlowEntity;
import com.sms.courier.mapper.ProjectImportFlowMapper;
import com.sms.courier.repository.ApiGroupRepository;
import com.sms.courier.repository.ApiRepository;
import com.sms.courier.repository.ProjectImportFlowRepository;
import com.sms.courier.service.impl.AsyncServiceImpl;
import com.sms.courier.websocket.Payload;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.util.Streamable;

@DisplayName("Test for AsyncService")
public class AsyncServiceTest {

    private final ApiRepository apiRepository = mock(ApiRepository.class);
    private final ApiGroupRepository apiGroupRepository = mock(ApiGroupRepository.class);
    private final ProjectImportFlowRepository projectImportFlowRepository = mock(ProjectImportFlowRepository.class);
    private final ProjectImportFlowMapper projectImportFlowMapper = mock(ProjectImportFlowMapper.class);
    private ApplicationContext applicationContext = mock(ApplicationContext.class);
    private final MessageService messageService = mock(MessageService.class);
    private final AsyncServiceImpl asyncService = new AsyncServiceImpl(apiRepository,
        apiGroupRepository, projectImportFlowRepository, projectImportFlowMapper,
        messageService);

    @BeforeEach
    public void beforeEach() {
        asyncService.setApplicationContext(applicationContext);
    }

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
        when(apiGroupRepository.findByProjectIdAndDepth(any(), any()))
            .thenReturn(getApiGroup());
        when(apiRepository.findApiEntitiesByProjectIdAndSwaggerIdNotNull(any()))
            .thenReturn(Streamable.of(getApi()));
        when(apiRepository.saveAll(any())).thenReturn(getApi());
        when(applicationContext.getBean(ProjectImportFlowRepository.class)).thenReturn(projectImportFlowRepository);
        when(applicationContext.getBean(MessageService.class)).thenReturn(messageService);
        when(applicationContext.getBean(ApiRepository.class)).thenReturn(apiRepository);
        doNothing().when(apiRepository).deleteAll();
        doNothing().when(applicationContext).publishEvent(any());
        when(apiRepository.saveAll(any())).thenReturn(getApi());
    }

    private Set<ApiGroupEntity> getApiGroup() {
        Set<ApiGroupEntity> set = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            set.add(ApiGroupEntity.builder().id(ObjectId.get().toString()).name("test" + Math.random()).build());
        }
        return set;
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
