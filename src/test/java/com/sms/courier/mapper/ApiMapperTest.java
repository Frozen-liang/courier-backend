package com.sms.courier.mapper;

import com.sms.courier.common.enums.ApiJsonType;
import com.sms.courier.common.enums.ApiProtocol;
import com.sms.courier.common.enums.DocumentUrlType;
import com.sms.courier.common.enums.RequestMethod;
import com.sms.courier.dto.request.ApiImportRequest;
import com.sms.courier.dto.request.ApiRequest;
import com.sms.courier.dto.response.ApiResponse;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.project.ProjectImportSourceEntity;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.sms.courier.common.enums.ApiJsonType.OBJECT;
import static com.sms.courier.common.enums.ApiProtocol.HTTP;
import static com.sms.courier.common.enums.ApiRequestParamType.JSON;
import static com.sms.courier.common.enums.ApiStatus.DEVELOP;
import static com.sms.courier.common.enums.RequestMethod.GET;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests for ApiMapper")
class ApiMapperTest {

    private final ApiMapper apiMapper = new ApiMapperImpl(new ParamInfoMapperImpl());

    private static final Integer SIZE = 10;
    private static final String API_NAME = "apiName";
    ApiEntity api = ApiEntity.builder().apiProtocol(ApiProtocol.HTTPS).requestMethod(RequestMethod.DELETE)
            .apiStatus(DEVELOP).apiRequestJsonType(ApiJsonType.ARRAY)
            .apiRequestParamType(JSON).apiRequestJsonType(ApiJsonType.ARRAY)
            .apiResponseJsonType(ApiJsonType.ARRAY).createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME).apiName(API_NAME).build();
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();
    private static final String API_TAG_ID = "1";

    @Test
    @DisplayName("Test the method to convert the Api's entity object to a dto object")
    void entity_to_dto() {
        ApiResponse apiDto = apiMapper.toDto(api);
        assertThat(apiDto.getApiProtocol()).isEqualTo(ApiProtocol.HTTPS.getCode());
        assertThat(apiDto.getRequestMethod()).isEqualTo(RequestMethod.DELETE.getCode());
        assertThat(apiDto.getApiStatus()).isEqualTo(DEVELOP.getCode());
        assertThat(apiDto.getCreateDateTime()).isEqualTo(CREATE_TIME);
        assertThat(apiDto.getModifyDateTime()).isEqualTo(MODIFY_TIME);
    }

    @Test
    @DisplayName("Test the method for converting an Api entity list object to a dto list object")
    void apiList_to_apiDtoList() {
        List<ApiEntity> apis = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            apis.add(api);
        }
        List<ApiResponse> apiDtoList = apiMapper.toDtoList(apis);
        assertThat(apiDtoList).hasSize(SIZE);
        assertThat(apiDtoList).allMatch(result -> StringUtils.equals(result.getApiName(), API_NAME));
    }

    @Test
    @DisplayName("Test the method to convert the Api's dto object to a entity object")
    void dto_to_entity() {
        ApiRequest apiDto = ApiRequest.builder()
                .apiProtocol(HTTP).apiRequestJsonType(OBJECT).apiRequestParamType(JSON)
                .apiStatus(DEVELOP).apiResponseJsonType(OBJECT).requestMethod(GET).build();
        ApiEntity api = apiMapper.toEntity(apiDto);
        assertThat(api.getApiProtocol().getCode()).isEqualTo(HTTP.getCode());
        assertThat(api.getApiRequestJsonType().getCode()).isEqualTo(OBJECT.getCode());
        assertThat(api.getApiRequestParamType().getCode()).isEqualTo(JSON.getCode());
        assertThat(api.getApiStatus().getCode()).isEqualTo(DEVELOP.getCode());
        assertThat(api.getApiResponseJsonType().getCode()).isEqualTo(OBJECT.getCode());
        assertThat(api.getRequestMethod().getCode()).isEqualTo(GET.getCode());
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the Api's entity object to a dto object")
    void null_entity_to_dto() {
        ApiResponse apiDto = apiMapper.toDto(null);
        assertThat(apiDto).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the Api's dto object to a entity object")
    void null_dto_to_entity() {
        ApiEntity api = apiMapper.toEntity(null);
        assertThat(api).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method for converting an Api entity list object to a dto list object")
    void null_entityList_to_dtoList() {
        List<ApiResponse> apiDtoList = apiMapper.toDtoList(null);
        assertThat(apiDtoList).isNull();
    }

    @Test
    void notNull_entityList_to_List() {
        ApiRequest toDto = ApiRequest.builder().apiName(API_NAME)
                .tagId(Lists.newArrayList(API_TAG_ID)).build();
        ApiEntity apiEntity = apiMapper.toEntity(toDto);
        assertThat(apiEntity).isNotNull();
    }

    @Test
    void Null_toImportSource() {
        ApiImportRequest request = ApiImportRequest.builder().documentFileType(0).saveMode(0).build();
        assertThat(apiMapper.toImportSource(request, "test")).isNotNull();
    }

    @Test
    void Null_toImportSource_project() {
        ProjectImportSourceEntity projectImportSourceEntity = ProjectImportSourceEntity.builder()
                .documentType(DocumentUrlType.SWAGGER_FILE)
                .build();
        assertThat(apiMapper.toImportSource(projectImportSourceEntity)).isNotNull();
    }

    @Test
    void Null_toImportSource_isNull() {
        assertThat(apiMapper.toImportSource(null, null)).isNull();

    }

    @Test
    void Null_toImportSource_projectNull() {
        assertThat(apiMapper.toImportSource(null)).isNull();
    }
}