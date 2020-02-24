package com.sms.satp.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.ApplicationTests;
import com.sms.satp.entity.StatusCodeDoc;
import com.sms.satp.entity.dto.StatusCodeDocDto;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@DisplayName("Tests for StatusCodeDocMapper")
@SpringBootTest(classes = ApplicationTests.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StatusCodeDocMapperTest {
    
    @SpyBean
    StatusCodeDocMapper statusCodeDocMapper;

    private static final String CODE = "title";
    private static final String CREATE_TIME_STRING = "2020-02-10 14:24:35";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the StatusCodeDoc's entity object to a dto object")
    void entity_to_dto() {
        StatusCodeDoc statusCodeDoc = StatusCodeDoc.builder()
            .code(CODE)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        StatusCodeDocDto statusCodeDocDto = statusCodeDocMapper.toDto(statusCodeDoc);
        assertThat(statusCodeDocDto.getCode()).isEqualTo(CODE);
    }

    @Test
    @DisplayName("Test the method to convert the StatusCodeDoc's dto object to a entity object")
    void dto_to_entity() {
        StatusCodeDocDto statusCodeDocDto = StatusCodeDocDto.builder()
            .code(CODE)
            .createDateTime(CREATE_TIME_STRING)
            .build();
        StatusCodeDoc statusCodeDoc = statusCodeDocMapper.toEntity(statusCodeDocDto);
        assertThat(statusCodeDoc.getCode()).isEqualTo(CODE);
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the StatusCodeDoc's entity object to a dto object")
    void null_entity_to_dto() {
        StatusCodeDocDto statusCodeDocDto = statusCodeDocMapper.toDto(null);
        assertThat(statusCodeDocDto).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the StatusCodeDoc's dto object to a entity object")
    void null_dto_to_entity() {
        StatusCodeDoc statusCodeDoc = statusCodeDocMapper.toEntity(null);
        assertThat(statusCodeDoc).isNull();
    }

}