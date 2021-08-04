package com.sms.satp.mapper;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.common.enums.ImportStatus;
import com.sms.satp.common.enums.ParamType;
import com.sms.satp.dto.request.ParamInfoRequest;
import com.sms.satp.dto.response.ParamInfoResponse;
import com.sms.satp.dto.response.ProjectImportFlowResponse;
import com.sms.satp.entity.api.common.ParamInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.sms.satp.entity.project.ProjectImportFlowEntity;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
