package com.sms.satp.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.common.enums.ApiBindingStatus;
import com.sms.satp.entity.apitestcase.ApiTestCaseEntity;
import com.sms.satp.repository.impl.CustomizedApiTestCaseRepositoryImpl;
import java.util.Collections;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

@DisplayName("Tests for CustomizedApiTestCaseRepository")
class CustomizedApiTestCaseRepositoryTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final CustomizedApiTestCaseRepository customizedApiTestCaseRepository =
        new CustomizedApiTestCaseRepositoryImpl(mongoTemplate,
            commonRepository);
    private static final String ID = ObjectId.get().toString();
    private static final List<String> ID_LIST = Collections.singletonList(ID);

    @Test
    @DisplayName("Test the updateApiTestCaseStatusByApiId method in the CustomizedApiTestCaseRepository")
    public void updateApiTestCaseStatusByApiId_test() {
        when(mongoTemplate.updateMulti(any(), any(), any(Class.class))).thenReturn(null);
        customizedApiTestCaseRepository
            .updateApiTestCaseStatusByApiId(Collections.singletonList(ObjectId.get().toString()),
                ApiBindingStatus.BINDING);
        verify(mongoTemplate, times(1)).updateMulti(any(), any(), any(Class.class));
    }

    @Test
    @DisplayName("Test the deleteById method in the CustomizedApiTestCaseRepository")
    public void deleteById_test() {
        when(commonRepository.deleteById(ID, ApiTestCaseEntity.class)).thenReturn(Boolean.TRUE);
        assertThat(customizedApiTestCaseRepository.deleteById(ID)).isTrue();
    }

    @Test
    @DisplayName("Test the deleteByIds method in the CustomizedApiTestCaseRepository")
    public void deleteByIds() {
        when(commonRepository.deleteByIds(ID_LIST, ApiTestCaseEntity.class)).thenReturn(Boolean.TRUE);
        assertThat(customizedApiTestCaseRepository.deleteByIds(ID_LIST)).isTrue();
    }
}
