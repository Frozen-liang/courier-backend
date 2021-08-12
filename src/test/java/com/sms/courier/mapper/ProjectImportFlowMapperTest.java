package com.sms.satp.mapper;

import com.sms.courier.common.enums.ImportStatus;
import com.sms.courier.dto.response.ProjectImportFlowResponse;
import com.sms.courier.entity.project.ProjectImportFlowEntity;
import com.sms.courier.mapper.ProjectImportFlowMapper;
import com.sms.courier.mapper.ProjectImportFlowMapperImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests for ProjectImportFlowMapper")
public class ProjectImportFlowMapperTest {

    private final ProjectImportFlowMapper projectImportFlowMapper = new ProjectImportFlowMapperImpl();
    private static final Integer SIZE = 10;
    private static final String DESCRIPTION = "description";
    private static final java.time.LocalDateTime START_TIME = java.time.LocalDateTime.now();
    private static final java.time.LocalDateTime END_TIME = java.time.LocalDateTime.now();;
    private static final java.time.LocalDateTime CREATE_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the toProjectImportFlowResponse's dto object to a entity object")
    void toProjectImportFlowResponse_Test() {
        ProjectImportFlowEntity entity = ProjectImportFlowEntity.builder()
                .id("ddd")
                .projectId("ddd")
                .importSourceId("ddd")
                .startTime(START_TIME)
                .endTime(END_TIME)
                .errorDetail("error")
                .importStatus(ImportStatus.RUNNING)
                .createDateTime(CREATE_TIME)
                .build();
        ProjectImportFlowResponse dto = projectImportFlowMapper.toProjectImportFlowResponse(entity);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the toProjectImportFlowResponse's dto object to a entity object")
    void toProjectImportFlowResponse_IsNull_Test(){
        ProjectImportFlowResponse dto = projectImportFlowMapper.toProjectImportFlowResponse(null);
        assertThat(dto).isNull();
    }

}
