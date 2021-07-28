package com.sms.satp.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertTrue;

import com.mongodb.client.result.UpdateResult;
import com.sms.satp.entity.scenetest.CaseTemplateEntity;
import com.sms.satp.entity.scenetest.SceneCaseApiEntity;
import com.sms.satp.repository.impl.CustomizedSceneCaseApiRepositoryImpl;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

@DisplayName("Tests for CustomizedSceneCaseApiRepositoryTest")
class CustomizedSceneCaseApiRepositoryTest {


    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final CustomizedSceneCaseApiRepository customizedSceneCaseApiRepository = new CustomizedSceneCaseApiRepositoryImpl(
        mongoTemplate,
        commonRepository);

    private final static String MOCK_ID = "1";

    @Test
    @DisplayName("Test the findSceneCaseApiByApiIds method in the CustomizedSceneCaseApiRepository")
    void findSceneCaseApiByApiIds_test() {
        when(mongoTemplate.find(any(), any()))
            .thenReturn(Lists.newArrayList(SceneCaseApiEntity.builder().build()));
        List<SceneCaseApiEntity> sceneCaseApiList = customizedSceneCaseApiRepository
            .findSceneCaseApiByApiIds(Lists.newArrayList(MOCK_ID));
        assertThat(sceneCaseApiList).isNotEmpty();
    }

    @Test
    @DisplayName("Test the deleteSceneCaseApiConn method in the CustomizedSceneCaseApiRepository")
    void deleteSceneCaseApiConn_test() {
        UpdateResult result = UpdateResult.unacknowledged();
        when(mongoTemplate.updateMulti(any(), any(), any(Class.class))).thenReturn(result);
        Boolean isSuccess = customizedSceneCaseApiRepository.deleteSceneCaseApiConn(Lists.newArrayList(MOCK_ID));
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the deleteByIds method in the CustomizedSceneCaseApiRepository")
    void deleteByIds_test() {
        when(commonRepository.deleteByIds(any(), any())).thenReturn(Boolean.TRUE);
        Boolean isSuccess = customizedSceneCaseApiRepository.deleteByIds(Lists.newArrayList(MOCK_ID));
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the deleteByIds method in the CustomizedSceneCaseApiRepository")
    void recover_test() {
        when(commonRepository.recover(any(), any())).thenReturn(Boolean.TRUE);
        Boolean isSuccess = customizedSceneCaseApiRepository.recover(Lists.newArrayList(MOCK_ID));
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the findSceneCaseApiIdsBySceneCaseIds method in the CustomizedSceneCaseApiRepository")
    void findSceneCaseApiIdsBySceneCaseIds_test() {
        when(mongoTemplate.find(any(), any()))
            .thenReturn(Lists.newArrayList(CaseTemplateEntity.builder().build()));
        List<SceneCaseApiEntity> dto =
            customizedSceneCaseApiRepository.findSceneCaseApiIdsBySceneCaseIds(Lists.newArrayList(MOCK_ID));
        assertThat(dto).isNotEmpty();
    }
}
