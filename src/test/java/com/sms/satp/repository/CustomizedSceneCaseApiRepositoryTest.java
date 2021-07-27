package com.sms.satp.repository;

import com.mongodb.client.result.UpdateResult;
import com.sms.satp.entity.scenetest.SceneCaseApiEntity;
import com.sms.satp.repository.impl.CustomizedSceneCaseApiRepositoryImpl;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertTrue;

@DisplayName("Tests for CustomizedSceneCaseApiRepositoryTest")
class CustomizedSceneCaseApiRepositoryTest {


    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final CommonDeleteRepository commonDeleteRepository = mock(CommonDeleteRepository.class);
    private final CustomizedSceneCaseApiRepository repository = new CustomizedSceneCaseApiRepositoryImpl(mongoTemplate,
        commonDeleteRepository);

    private final static String MOCK_ID = "1";

    @Test
    @DisplayName("Test the findSceneCaseApiByApiIds method in the CustomizedSceneCaseApiRepository")
    void findSceneCaseApiByApiIds_test() {
        when(mongoTemplate.find(any(), any()))
            .thenReturn(Lists.newArrayList(SceneCaseApiEntity.builder().build()));
        List<SceneCaseApiEntity> sceneCaseApiList = repository.findSceneCaseApiByApiIds(Lists.newArrayList(MOCK_ID));
        assertThat(sceneCaseApiList).isNotEmpty();
    }

    @Test
    @DisplayName("Test the findSceneCaseApiBySceneCaseIdAndIsExecute method in the CustomizedSceneCaseApiRepository")
    void findSceneCaseApiBySceneCaseIdAndIsExecute_test() {
        when(mongoTemplate.find(any(), any()))
            .thenReturn(Lists.newArrayList(SceneCaseApiEntity.builder().build()));
        List<SceneCaseApiEntity> sceneCaseApiList = repository
            .findSceneCaseApiBySceneCaseIdAndIsExecuteAndIsRemove(MOCK_ID, Boolean.TRUE,Boolean.FALSE);
        assertThat(sceneCaseApiList).isNotEmpty();
    }

    @Test
    @DisplayName("Test the deleteSceneCaseApiConn method in the CustomizedSceneCaseApiRepository")
    void deleteSceneCaseApiConn_test() {
        UpdateResult result = UpdateResult.unacknowledged();
        when(mongoTemplate.updateMulti(any(), any(), any(Class.class))).thenReturn(result);
        Boolean isSuccess = repository.deleteSceneCaseApiConn(Lists.newArrayList(MOCK_ID));
        assertTrue(isSuccess);
    }

}
