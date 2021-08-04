package com.sms.satp.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.common.enums.ApiStatus;
import com.sms.satp.common.enums.DocumentUrlType;
import com.sms.satp.common.enums.ParamType;
import com.sms.satp.common.enums.SaveMode;
import com.sms.satp.dto.request.ProjectImportSourceRequest;
import com.sms.satp.dto.response.ProjectImportSourceResponse;
import com.sms.satp.entity.api.common.ParamInfo;

import com.sms.satp.entity.project.ProjectImportSourceEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

@DisplayName("Tests for ProjectImportSourceMapper")
public class ProjectImportSourceMapperTest {

    private final ProjectImportSourceMapper projectImportSourceMapper = new ProjectImportSourceMapperImpl();
    private final ParamInfo paramInfo =
            ParamInfo.builder().paramType(ParamType.STRING).description(DESCRIPTION).build();
    private static final Integer SIZE = 10;
    private static final String DESCRIPTION = "description";
    private static final LocalDateTime CREATE_TIME=LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME=LocalDateTime.now();


    @Test
    @DisplayName("Test the method to convert the ParamInfo's dto object to a entity object")
    void toProjectImportSourceEntity_Test(){
        ProjectImportSourceRequest request=ProjectImportSourceRequest.builder()
                .id("id")
                .name("name")
                .projectId("project")
                .documentUrl("Url")
                .build();
        ProjectImportSourceEntity dto = projectImportSourceMapper.toProjectImportSourceEntity(request);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the ParamInfo's dto object to a entity object")
    void toProjectImportSourceEntity_IsNull_Test(){
        ProjectImportSourceEntity dto = projectImportSourceMapper.toProjectImportSourceEntity(null);
        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Test the method to convert the ParamInfo's dto object to a entity object")
    void toProjectImportSourceResponse_Test(){
        ProjectImportSourceEntity entity = ProjectImportSourceEntity.builder()
                .id("id")
                .name("name")
                .projectId("project")
                .documentUrl("Url")
                .createDateTime(CREATE_TIME)
                .modifyDateTime(MODIFY_TIME)
                .documentType(DocumentUrlType.SWAGGER_FILE)
                .saveMode(SaveMode.COVER)
                .apiPresetStatus(ApiStatus.DEPRECATED)
                .apiChangeStatus(ApiStatus.DEPRECATED)
                .build();
        ProjectImportSourceResponse dto = projectImportSourceMapper.toProjectImportSourceResponse(entity);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the ParamInfo's dto object to a entity object")
    void toProjectImportSourceResponse_IsNull_Test(){
        ProjectImportSourceResponse dto = projectImportSourceMapper.toProjectImportSourceResponse(null);
        assertThat(dto).isNull();
    }


}
