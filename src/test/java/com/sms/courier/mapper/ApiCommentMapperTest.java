package com.sms.courier.mapper;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.courier.dto.request.ApiCommentRequest;
import com.sms.courier.dto.response.ApiCommentResponse;
import com.sms.courier.entity.api.ApiCommentEntity;
import com.sms.courier.utils.MustacheUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for ApiCommentMapper")
class ApiCommentMapperTest {

    private final ApiCommentMapper apiCommentMapper = new ApiCommentMapperImpl();

    private static final Integer SIZE = 10;
    private static final String COMMENT = "apiComment";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the ApiComment's entity object to a dto object")
    void entity_to_dto() throws InterruptedException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(NON_NULL);
        Map<String, String> map = Map.of("key", objectMapper.writeValueAsString(ApiCommentEntity.builder().comment(
            "1").build()));
        System.out.println(MustacheUtils.getContent("--- {{key}}  ---", map));
        /*ApiCommentEntity apiComment = ApiCommentEntity.builder()
            .comment(COMMENT)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        ApiCommentResponse apiCommentResponse = apiCommentMapper.toDto(apiComment);
        assertThat(apiCommentResponse.getComment()).isEqualTo(COMMENT);*/
    }

    @Test
    @DisplayName("Test the method for converting an ApiComment entity list object to a dto list object")
    void apiCommentList_to_apiCommentDtoList() {
        List<ApiCommentEntity> apiComments = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            apiComments.add(ApiCommentEntity.builder().comment(COMMENT).build());
        }
        List<ApiCommentResponse> apiCommentResponseList = apiCommentMapper.toDtoList(apiComments);
        assertThat(apiCommentResponseList).hasSize(SIZE);
        assertThat(apiCommentResponseList).allMatch(result -> StringUtils.equals(result.getComment(), COMMENT));
    }

    @Test
    @DisplayName("Test the method to convert the ApiComment's dto object to a entity object")
    void dto_to_entity() {
        ApiCommentRequest apiCommentRequest = ApiCommentRequest.builder()
            .comment(COMMENT)
            .build();
        ApiCommentEntity apiComment = apiCommentMapper.toEntity(apiCommentRequest);
        assertThat(apiComment.getComment()).isEqualTo(COMMENT);
    }

}
