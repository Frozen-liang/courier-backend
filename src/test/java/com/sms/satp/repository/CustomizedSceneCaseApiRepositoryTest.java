package com.sms.satp.repository;

import com.sms.satp.entity.scenetest.SceneCaseApi;
import com.sms.satp.repository.impl.CustomizedSceneCaseApiRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Tests for CustomizedSceneCaseApiRepositoryTest")
class CustomizedSceneCaseApiRepositoryTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final CustomizedSceneCaseApiRepository repository = new CustomizedSceneCaseApiRepositoryImpl(mongoTemplate);

    private final static String MOCK_ID = "1";

    @Test
    @DisplayName("Test the findMaxOrderBySceneCaseId method in the CustomizedSceneCaseApiRepository")
    void findMaxOrderBySceneCaseId_test() {
        SceneCaseApi sceneCaseApi = SceneCaseApi.builder().id(MOCK_ID).build();
        when(mongoTemplate.findOne(any(),eq(SceneCaseApi.class))).thenReturn(sceneCaseApi);
        SceneCaseApi result = repository.findMaxOrderBySceneCaseId(MOCK_ID);
        assertThat(result).isNotNull();
    }

}
