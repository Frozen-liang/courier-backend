package com.sms.satp.service;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.response.ApiGroupResponse;
import com.sms.satp.entity.group.ApiGroupEntity;
import com.sms.satp.mapper.ApiGroupMapper;
import com.sms.satp.repository.ApiGroupRepository;
import com.sms.satp.service.impl.ApiGroupServiceImpl;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sms.satp.common.exception.ErrorCode.GET_API_GROUP_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Test cases for ApiGroupServiceTest")
class ApiGroupServiceTest {

    private final ApiGroupRepository apiGroupRepository = mock(ApiGroupRepository.class);
    private final ApiGroupMapper apiGroupMapper = mock(ApiGroupMapper.class);
    private final ApiGroupService apiGroupService = new ApiGroupServiceImpl(apiGroupRepository, apiGroupMapper);
    private final static String MOCK_ID = "1";

    @Test
    @DisplayName("Test the list method in the ApiGroup service")
    void list_test() {
        List<ApiGroupEntity> apiGroupEntityList = Lists.newArrayList(ApiGroupEntity.builder().id(MOCK_ID).build());
        when(apiGroupRepository.findApiGroupEntitiesByProjectId(any())).thenReturn(apiGroupEntityList);
        List<ApiGroupResponse> apiGroupResponseList =
            Lists.newArrayList(ApiGroupResponse.builder().id(MOCK_ID).build());
        when(apiGroupMapper.toResponse(any())).thenReturn(apiGroupResponseList);
        List<ApiGroupResponse> dtoResponse = apiGroupService.list(MOCK_ID);
        assertThat(dtoResponse).isNotEmpty();
    }

    @Test
    @DisplayName("Test the list method in the ApiGroup service thrown exception")
    void list_test_thrownException() {
        when(apiGroupRepository.findApiGroupEntitiesByProjectId(any()))
            .thenThrow(new ApiTestPlatformException(GET_API_GROUP_LIST_ERROR));
        assertThatThrownBy(() -> apiGroupService.list(MOCK_ID)).isInstanceOf(ApiTestPlatformException.class);
    }

}
