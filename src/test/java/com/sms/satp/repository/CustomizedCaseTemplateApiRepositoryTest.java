package com.sms.satp.repository;

import com.sms.satp.entity.scenetest.CaseTemplateApiEntity;
import com.sms.satp.repository.impl.CustomizedCaseTemplateApiRepositoryImpl;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Tests for CustomizedCaseTemplateApiRepositoryTest")
class CustomizedCaseTemplateApiRepositoryTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final CustomizedCaseTemplateApiRepository customizedCaseTemplateApiRepository =
        new CustomizedCaseTemplateApiRepositoryImpl(mongoTemplate);

    private final static String MOCK_ID = "1";
    private final static String NAME = "test";
    private final static Long COUNT = 1L;

    @Test
    @DisplayName("Test the findByCaseTemplateIds method in the CustomizedCaseTemplateApiRepository")
    void findByCaseTemplateIds_test() {
        when(mongoTemplate.find(any(), any()))
            .thenReturn(Lists.newArrayList(CaseTemplateApiEntity.builder().build()));
        List<CaseTemplateApiEntity> dto =
            customizedCaseTemplateApiRepository.findByCaseTemplateIds(Lists.newArrayList(MOCK_ID));
        assertThat(dto).isNotEmpty();
    }

    @Test
    @DisplayName("Test the findByCaseTemplateIds method in the CustomizedCaseTemplateApiRepository")
    void findByCaseTemplateIds_test_thenNull() {
        List<CaseTemplateApiEntity> dto =
            customizedCaseTemplateApiRepository.findByCaseTemplateIds(Lists.newArrayList());
        assertThat(dto).isEmpty();
    }

    @Test
    @DisplayName("Test the findByCaseTemplateIdAndIsExecute method in the CustomizedCaseTemplateApiRepository")
    void findByCaseTemplateIdAndIsExecute_test() {
        when(mongoTemplate.find(any(), any()))
            .thenReturn(Lists.newArrayList(CaseTemplateApiEntity.builder().build()));
        List<CaseTemplateApiEntity> dto =
            customizedCaseTemplateApiRepository.findByCaseTemplateIdAndIsExecute(MOCK_ID,Boolean.TRUE);
        assertThat(dto).isNotEmpty();
    }
}
