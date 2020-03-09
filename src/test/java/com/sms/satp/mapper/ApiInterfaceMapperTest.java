package com.sms.satp.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.ApplicationTests;
import com.sms.satp.entity.ApiInterface;
import com.sms.satp.entity.Header;
import com.sms.satp.entity.Parameter;
import com.sms.satp.entity.dto.ApiInterfaceDto;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@DisplayName("Tests for ApiInterfaceMapper")
@SpringBootTest(classes = ApplicationTests.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiInterfaceMapperTest {

    @SpyBean
    ApiInterfaceMapper apiInterfaceMapper;

    private static final Integer SIZE = 10;
    private static final String TITLE = "title";
    private static final List<String> TAG_LIST = Arrays.asList("tag1","tag2");
    private static final List<Header> HEADER_LIST = Collections.singletonList(Header.builder().build());
    private static final List<Parameter> PARAMETER_LIST = Collections.singletonList(Parameter.builder().build());

    @Test
    @DisplayName("Test the method to convert the ApiInterface's entity object to a dto object")
    void entity_to_dto() {
        ApiInterface apiInterface = ApiInterface.builder()
            .title(TITLE)
            .tag(TAG_LIST)
            .requestHeaders(HEADER_LIST)
            .responseHeaders(HEADER_LIST)
            .queryParams(PARAMETER_LIST)
            .pathParams(PARAMETER_LIST)
            .build();
        ApiInterfaceDto apiInterfaceDto = apiInterfaceMapper.toDto(apiInterface);
        assertThat(apiInterfaceDto.getTitle()).isEqualTo(TITLE);
    }

    @Test
    @DisplayName("Test the method to convert the ApiInterface's dto object to a entity object")
    void dto_to_entity() {
        ApiInterfaceDto apiInterfaceDto = ApiInterfaceDto.builder()
            .title(TITLE)
            .tag(TAG_LIST)
            .requestHeaders(HEADER_LIST)
            .responseHeaders(HEADER_LIST)
            .queryParams(PARAMETER_LIST)
            .pathParams(PARAMETER_LIST)
            .build();
        ApiInterface apiInterface = apiInterfaceMapper.toEntity(apiInterfaceDto);
        assertThat(apiInterface.getTitle()).isEqualTo(TITLE);
    }

    @Test
    @DisplayName("Test the method to convert the ApiInterface's entity object to a dto page object")
    void entity_to_dtoPage() {
        ApiInterface apiInterface = ApiInterface.builder()
            .title(TITLE)
            .tag(TAG_LIST)
            .requestHeaders(HEADER_LIST)
            .responseHeaders(HEADER_LIST)
            .queryParams(PARAMETER_LIST)
            .pathParams(PARAMETER_LIST)
            .build();
        ApiInterfaceDto apiInterfaceDto = apiInterfaceMapper.toDtoPage(apiInterface);
        assertThat(apiInterfaceDto.getTitle()).isEqualTo(TITLE);
        assertThat(apiInterfaceDto.getRequestBody()).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the ApiInterface's entity object to a dto object")
    void null_entity_to_dto() {
        ApiInterfaceDto apiInterfaceDto = apiInterfaceMapper.toDto(null);
        assertThat(apiInterfaceDto).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the ApiInterface's dto object to a entity object")
    void null_dto_to_entity() {
        ApiInterface apiInterface = apiInterfaceMapper.toEntity(null);
        assertThat(apiInterface).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the ApiInterface's entity object to a dto page object")
    void null_entity_to_dtoPage() {
        ApiInterfaceDto apiInterfaceDto = apiInterfaceMapper.toDtoPage(null);
        assertThat(apiInterfaceDto).isNull();
    }

}