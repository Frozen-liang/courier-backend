package com.sms.courier.mapper;

import com.google.common.collect.Lists;
import com.sms.courier.common.enums.*;
import com.sms.courier.dto.request.*;
import com.sms.courier.dto.response.SceneCaseApiConnResponse;
import com.sms.courier.dto.response.SceneCaseApiResponse;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.group.SceneCaseGroupEntity;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.mockito.Mockito.mock;

@DisplayName("Tests for SceneCaseGroupMapper")
public class SceneCaseGroupMapperTest {

    private SceneCaseGroupMapper sceneCaseGroupMapper = new SceneCaseGroupMapperImpl();
    private static final String MOCK_ID = "1";
    private static final Integer SIZE = 1;

    @Test
    @DisplayName("Test the method to convert the Project's entity object to a dto object")
    void toSceneCaseGroupEntity_Test(){
        SceneCaseGroupRequest request = SceneCaseGroupRequest.builder().build();
        SceneCaseGroupEntity dto = sceneCaseGroupMapper.toSceneCaseGroupEntity(request);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the Project's entity object to a dto object")
    void toSceneCaseGroupEntity_IsNull_Test(){
        SceneCaseGroupEntity dto = sceneCaseGroupMapper.toSceneCaseGroupEntity(null);
        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Test the method to convert the Project's entity object to a dto object")
    void toResponse_Test(){
        List<SceneCaseGroupEntity> sceneCaseGroupEntityList = Lists.newArrayList(SceneCaseGroupEntity
                .builder().build());
        assertThat(sceneCaseGroupMapper.toResponse(sceneCaseGroupEntityList)).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the Project's entity object to a dto object")
    void sceneCaseGroupEntityToSceneCaseGroupResponse_IsNull_Test(){

        SceneCaseGroupEntity sceneCaseGroupEntity = null;
        List<SceneCaseGroupEntity> sceneCaseGroupEntityList = Lists.newArrayList(sceneCaseGroupEntity);
        assertThat(sceneCaseGroupMapper.toResponse(sceneCaseGroupEntityList)).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the Project's entity object to a dto object")
    void toResponse_IsNull_Test(){
        assertThat(sceneCaseGroupMapper.toResponse(null)).isNull();
    }


}
