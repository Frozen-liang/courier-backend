package com.sms.satp.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.ApplicationTests;
import com.sms.satp.entity.Schema;
import com.sms.satp.entity.dto.SchemaDto;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@DisplayName("Tests for ProjectMapper")
@SpringBootTest(classes = ApplicationTests.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SchemaMapperTest {
    
    @SpyBean
    SchemaMapper schemaMapper;

    private static final String TITLE = "title";
    private static final String CREATE_TIME_STRING = "2020-02-10 14:24:35";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();
    private static final List<String> REQUIRE = Collections.singletonList("require");
    private static final Map<String, Schema> PROPERTIES = new HashMap<>();

    @Test
    @DisplayName("Test the method to convert the Schema's entity object to a dto object")
    void entity_to_dto() {
        Schema schema = Schema.builder()
            .title(TITLE)
            .required(REQUIRE)
            .properties(PROPERTIES)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        SchemaDto schemaDto = schemaMapper.toDto(schema);
        assertThat(schemaDto.getTitle()).isEqualTo(TITLE);
    }

    @Test
    @DisplayName("Test the method to convert the Schema's dto object to a entity object")
    void dto_to_entity() {
        SchemaDto schemaDto = SchemaDto.builder()
            .title(TITLE)
            .required(REQUIRE)
            .properties(PROPERTIES)
            .createDateTime(CREATE_TIME_STRING)
            .build();
        Schema schema = schemaMapper.toEntity(schemaDto);
        assertThat(schema.getTitle()).isEqualTo(TITLE);
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the Schema's entity object to a dto object")
    void null_entity_to_dto() {
        SchemaDto schemaDto = schemaMapper.toDto(null);
        assertThat(schemaDto).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the Schema's dto object to a entity object")
    void null_dto_to_entity() {
        Schema schema = schemaMapper.toEntity(null);
        assertThat(schema).isNull();
    }

}