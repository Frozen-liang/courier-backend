package com.sms.courier.mapper;

import com.sms.courier.common.enums.ApiJsonType;
import com.sms.courier.common.enums.ApiProtocol;
import com.sms.courier.common.enums.ApiRequestParamType;
import com.sms.courier.common.enums.RequestMethod;
import com.sms.courier.dto.request.ApiRequest;
import com.sms.courier.dto.request.ApiTestCaseRequest;
import com.sms.courier.dto.response.ApiTestCaseResponse;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("Tests for ApiTestCaseMapper")
class ApiTestCaseMapperTest {

    private ResponseResultVerificationMapper responseResultVerificationMapper =
        mock(ResponseResultVerificationMapper.class);
    private ResponseHeadersVerificationMapper responseHeadersVerificationMapper =
        mock(ResponseHeadersVerificationMapper.class);
    private ParamInfoMapper paramInfoMapper = new ParamInfoMapperImpl();
    private ApiTestCaseMapper apiTestCaseMapper = new ApiTestCaseMapperImpl(responseResultVerificationMapper,
        responseHeadersVerificationMapper, paramInfoMapper);
    private ApiTestCaseEntity apiTestCase = ApiTestCaseEntity.builder()
        .caseName(CASE_NAME).apiEntity(ApiEntity.builder()
            .apiRequestJsonType(ApiJsonType.OBJECT)
            .apiResponseJsonType(ApiJsonType.ARRAY)
            .requestMethod(RequestMethod.GET)
            .apiRequestParamType(ApiRequestParamType.JSON)
            .apiProtocol(ApiProtocol.HTTP).build())
        .createDateTime(CREATE_TIME).modifyDateTime(MODIFY_TIME)
        .build();
    private static final Integer SIZE = 10;
    private static final String CASE_NAME = "apiTestCase";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the ApiTestCase's entity object to a dto object")
    void entity_to_dto() {
        ApiTestCaseResponse apiTestCaseResponse = apiTestCaseMapper.toDto(apiTestCase);
        assertThat(apiTestCaseResponse.getCaseName()).isEqualTo(CASE_NAME);
        assertThat(apiTestCaseResponse.getApiEntity().getApiProtocol()).isEqualTo(ApiProtocol.HTTP.getCode());
    }

    @Test
    @DisplayName("Test the method for converting an ApiTestCase entity list object to a dto list object")
    void apiTestCaseList_to_apiTestCaseDtoList() {
        List<ApiTestCaseEntity> apiTestCases = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            apiTestCases.add(apiTestCase);
        }
        List<ApiTestCaseResponse> apiTestCaseResponseList = apiTestCaseMapper.toDtoList(apiTestCases);
        assertThat(apiTestCaseResponseList).hasSize(SIZE);
        assertThat(apiTestCaseResponseList).allMatch(result -> StringUtils.equals(result.getCaseName(), CASE_NAME));
    }

    @Test
    @DisplayName("Test the method to convert the ApiTestCase's dto object to a entity object")
    void dto_to_entity() {
        ApiTestCaseRequest apiTestCaseRequest = ApiTestCaseRequest.builder()
            .caseName(CASE_NAME)
            .build();
        ApiTestCaseEntity apiTestCase = apiTestCaseMapper.toEntity(apiTestCaseRequest);
        assertThat(apiTestCase.getCaseName()).isEqualTo(CASE_NAME);
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the ApiTestCase's entity object to a dto object")
    void null_entity_to_dto() {
        ApiTestCaseResponse apiTestCaseResponse = apiTestCaseMapper.toDto(null);
        assertThat(apiTestCaseResponse).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the ApiTestCase's dto object to a entity object")
    void null_dto_to_entity() {
        ApiTestCaseEntity apiTestCase = apiTestCaseMapper.toEntity(null);
        assertThat(apiTestCase).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method for converting an ApiTestCase entity list object to a dto list object")
    void null_entityList_to_dtoList() {
        List<ApiTestCaseResponse> apiTestCaseDtoList = apiTestCaseMapper.toDtoList(null);
        assertThat(apiTestCaseDtoList).isNull();
    }

    @Test
    @DisplayName("[NotNull Input Parameter]Test the method for converting an ApiTestCase entity list object to a dto list object")
    void Notnull_entityList_to_dtoList() {
        ApiTestCaseRequest apiTestCaseRequest = ApiTestCaseRequest.builder()
            .tagId(Lists.newArrayList())
            .apiEntity(ApiRequest.builder()
                .requestHeaders(Lists.newArrayList())
                .responseHeaders(Lists.newArrayList())
                .pathParams(Lists.newArrayList())
                .restfulParams(Lists.newArrayList())
                .requestParams(Lists.newArrayList())
                .responseParams(Lists.newArrayList())
                .build())
            .build();
        ApiTestCaseEntity dto = apiTestCaseMapper.toEntity(apiTestCaseRequest);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("test the method an ApiTestCaseEntity to EntityByApiEntity")
    void ApiTestCaseEntity_toEntityByApiEntity() {
        ApiEntity apiEntity = ApiEntity.builder().build();
        assertThat(apiTestCaseMapper.toEntityByApiEntity(apiEntity)).isNotNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]test the method an ApiTestCaseEntity to EntityByApiEntity")
    void Null_ApiTestCaseEntity_toEntityByApiEntity() {
        assertThat(apiTestCaseMapper.toEntityByApiEntity(null)).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]test the method an ApiTestCaseEntity to EntityByApiEntity")
    void NotNull_ApiTestCaseEntity_toEntityByApiEntity() {
        ApiEntity apiEntity = ApiEntity.builder()
            .requestHeaders(Lists.newArrayList())
            .responseHeaders(Lists.newArrayList())
            .pathParams(Lists.newArrayList())
            .restfulParams(Lists.newArrayList())
            .requestParams(Lists.newArrayList())
            .responseParams(Lists.newArrayList())
            .build();
        assertThat(apiTestCaseMapper.toEntityByApiEntity(apiEntity)).isNotNull();
    }

}