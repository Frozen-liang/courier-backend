package com.sms.courier.repository;

import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import com.sms.courier.repository.impl.CustomizedCaseTemplateApiRepositoryImpl;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertTrue;

@DisplayName("Tests for CustomizedCaseTemplateApiRepositoryTest")
class CustomizedCaseTemplateApiRepositoryTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final CustomizedCaseTemplateApiRepository customizedCaseTemplateApiRepository =
        new CustomizedCaseTemplateApiRepositoryImpl(mongoTemplate, commonRepository);

    private final static String MOCK_ID = "1";
    private final static String NAME = "test";
    private final static Long COUNT = 1L;

    @Test
    @DisplayName("Test the findByCaseTemplateIdAndIsExecute method in the CustomizedCaseTemplateApiRepository")
    void findByCaseTemplateIdAndIsExecute_test() {
        when(mongoTemplate.find(any(), any()))
            .thenReturn(Lists.newArrayList(CaseTemplateApiEntity.builder().build()));
        List<CaseTemplateApiEntity> dto =
            customizedCaseTemplateApiRepository.findByCaseTemplateIdAndIsExecuteAndIsRemove(MOCK_ID, Boolean.TRUE,
                Boolean.FALSE);
        assertThat(dto).isNotEmpty();
    }

    @Test
    @DisplayName("Test the findCaseTemplateApiIdsByCaseTemplateIds method in the CustomizedCaseTemplateApiRepository")
    void findCaseTemplateApiIdsByCaseTemplateIds_test() {
        when(mongoTemplate.find(any(), any()))
            .thenReturn(Lists.newArrayList(CaseTemplateApiEntity.builder().build()));
        List<CaseTemplateApiEntity> dto =
            customizedCaseTemplateApiRepository.findCaseTemplateApiIdsByCaseTemplateIds(Lists.newArrayList(MOCK_ID));
        assertThat(dto).isNotEmpty();
    }

    @Test
    @DisplayName("Test the deleteByIds method in the CustomizedCaseTemplateApiRepository")
    void deleteByIds_test() {
        when(commonRepository.deleteByIds(any(),any())).thenReturn(Boolean.TRUE);
        Boolean isSuccess = customizedCaseTemplateApiRepository.deleteByIds(Lists.newArrayList(MOCK_ID));
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the deleteByIds method in the CustomizedCaseTemplateApiRepository")
    void recover_test() {
        when(commonRepository.recover(any(),any())).thenReturn(Boolean.TRUE);
        Boolean isSuccess = customizedCaseTemplateApiRepository.recover(Lists.newArrayList(MOCK_ID));
        assertTrue(isSuccess);
    }

}
