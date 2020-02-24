package com.sms.satp.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.ApplicationTests;
import com.sms.satp.entity.Wiki;
import com.sms.satp.entity.dto.WikiDto;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@DisplayName("Tests for WikiMapper")
@SpringBootTest(classes = ApplicationTests.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WikiMapperTest {
    
    @SpyBean
    WikiMapper wikiMapper;

    private static final String TITLE = "title";
    private static final String CREATE_TIME_STRING = "2020-02-10 14:24:35";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the Wiki's entity object to a dto object")
    void entity_to_dto() {
        Wiki wiki = Wiki.builder()
            .title(TITLE)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        WikiDto wikiDto = wikiMapper.toDto(wiki);
        assertThat(wikiDto.getTitle()).isEqualTo(TITLE);
    }

    @Test
    @DisplayName("Test the method to convert the Wiki's dto object to a entity object")
    void dto_to_entity() {
        WikiDto wikiDto = WikiDto.builder()
            .title(TITLE)
            .createDateTime(CREATE_TIME_STRING)
            .build();
        Wiki wiki = wikiMapper.toEntity(wikiDto);
        assertThat(wiki.getTitle()).isEqualTo(TITLE);
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the Wiki's entity object to a dto object")
    void null_entity_to_dto() {
        WikiDto wikiDto = wikiMapper.toDto(null);
        assertThat(wikiDto).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the Wiki's dto object to a entity object")
    void null_dto_to_entity() {
        Wiki wiki = wikiMapper.toEntity(null);
        assertThat(wiki).isNull();
    }

}