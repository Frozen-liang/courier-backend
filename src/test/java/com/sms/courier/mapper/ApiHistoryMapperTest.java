package com.sms.courier.mapper;


import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.api.common.ApiHistoryDetail;
import com.sms.courier.entity.api.common.ParamInfo;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests for ApiHistoryMapperTest")
public class ApiHistoryMapperTest {
    private final ApiHistoryMapper apiHistoryMapper = new ApiHistoryMapperImpl();
    private static final String API_NAME = "apiName";
    private static final String API_TAG_ID = "1";

    @Test
    void toApiHistoryDetailDto() {
        ApiEntity apiEntity = ApiEntity.builder().apiName(API_NAME).build();
        ApiHistoryDetail apiHistoryDetail = apiHistoryMapper.toApiHistoryDetail(apiEntity);
        assertThat(apiHistoryDetail.getApiName()).isEqualTo(API_NAME);
    }

    @Test
    void apiHistory_EntityIsnull() {

        assertThat(apiHistoryMapper.toApiHistoryDetail(null)).isNull();
    }

    @Test
    void apiHistory_List_notnull() {
        ApiEntity apiEntity = ApiEntity.builder().apiName(API_NAME)
                .tagId(Lists.newArrayList(API_TAG_ID))
                .requestHeaders(Lists.newArrayList(ParamInfo.builder().build()))
                .responseHeaders(Lists.newArrayList(ParamInfo.builder().build()))
                .pathParams(Lists.newArrayList(ParamInfo.builder().build()))
                .restfulParams(Lists.newArrayList(ParamInfo.builder().build()))
                .requestParams(Lists.newArrayList(ParamInfo.builder().build()))
                .responseParams(Lists.newArrayList(ParamInfo.builder().build()))
                .build();
        assertThat(apiHistoryMapper.toApiHistoryDetail(apiEntity));
    }

}
