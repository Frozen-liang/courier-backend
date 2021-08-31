package com.sms.courier.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.courier.dto.response.ApiGroupResponse;
import com.sms.courier.entity.group.ApiGroupEntity;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for ApiGroupMapperTest")
public class ApiGroupMapperTest {

    private final ApiGroupMapper apiGroupMapper = new ApiGroupMapperImpl();
    private static final Integer SIZE = 1;
    private static final String API_NAME = "apiName";

    @Test
    void apiList_to_apiDtoList() {
        File file = new File("D:\\IdeaProjects\\sms-satp\\src\\test\\resources\\config\\test.json");
        Map<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < 4000; i++) {
            map.put("key" + i, "");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(map);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            IOUtils.write(json,fileOutputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }/*
        List<ApiGroupEntity> apis = getEntity();
        List<ApiGroupResponse> apiDtoList = apiGroupMapper.toResponse(apis);
        assertThat(apiDtoList).hasSize(SIZE);
        assertThat(apiDtoList).allMatch(result -> StringUtils.equals(result.getName(), API_NAME));*/
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
