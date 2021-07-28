package com.sms.satp.mapper;

import com.sms.satp.dto.response.ApiGroupResponse;
import com.sms.satp.entity.group.ApiGroupEntity;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests for ApiGroupMapperTest")
public class ApiGroupMapperTest {

    private final ApiGroupMapper apiGroupMapper = new ApiGroupMapperImpl();
    private static final Integer SIZE = 1;
    private static final String API_NAME = "apiName";

    @Test
    void apiList_to_apiDtoList() {
        List<ApiGroupEntity> apis = getEntity();
        List<ApiGroupResponse> apiDtoList = apiGroupMapper.toResponse(apis);
        assertThat(apiDtoList).hasSize(SIZE);
        assertThat(apiDtoList).allMatch(result -> StringUtils.equals(result.getName(), API_NAME));
    }

    @Test
    void apiRequestListIsNull_to_apiDtoList() {
        List<ApiGroupResponse> apiGroupEntityList = apiGroupMapper.toResponse(null);
        assertThat(apiGroupEntityList).isNull();

    }

    @Test
    void apiRequestIsNull_to_apiDtoList() {
        ApiGroupEntity entity = null;
        List<ApiGroupEntity> apis = Lists.newArrayList(entity);
        List<ApiGroupResponse> apiGroupEntity = apiGroupMapper.toResponse(apis);
        assertThat(apiGroupEntity).size().isEqualTo(1);
    }

    private List<ApiGroupEntity> getEntity() {
        return Lists.newArrayList(ApiGroupEntity.builder().name(API_NAME).build());
    }
}
