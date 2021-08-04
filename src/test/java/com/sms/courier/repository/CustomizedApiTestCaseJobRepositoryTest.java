package com.sms.courier.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.dto.request.ApiTestCaseJobPageRequest;
import com.sms.courier.entity.job.ApiTestCaseJobEntity;
import com.sms.courier.repository.impl.CustomizedApiTestCaseJobRepositoryImpl;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;

@DisplayName("Tests for  CustomizedApiTestCaseJobRepository")
public class CustomizedApiTestCaseJobRepositoryTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final CustomizedApiTestCaseJobRepository customizedApiTestCaseJobRepository =
        new CustomizedApiTestCaseJobRepositoryImpl(mongoTemplate);
    private final ApiTestCaseJobPageRequest apiTestCaseJobPageRequest = new ApiTestCaseJobPageRequest();

    @Test
    @DisplayName("Test the search method in the CustomizedCaseTemplateRepository")
    void page_test() {
        when(mongoTemplate.count(any(BasicQuery.class), any(Class.class))).thenReturn(1L);
        when(mongoTemplate.find(any(BasicQuery.class), any(Class.class)))
            .thenReturn(Collections.singletonList(ApiTestCaseJobEntity.builder().build()));
        Page<ApiTestCaseJobEntity> page = customizedApiTestCaseJobRepository.page(apiTestCaseJobPageRequest);
        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isEqualTo(1L);
    }
}
