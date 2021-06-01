package com.sms.satp.repository;

import com.sms.satp.dto.PageDto;
import com.sms.satp.entity.job.SceneCaseJob;
import com.sms.satp.repository.impl.CustomizedSceneCaseJobRepositoryImpl;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Tests for CustomizedSceneCaseJobRepositoryTest")
class CustomizedSceneCaseJobRepositoryTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final CustomizedSceneCaseJobRepository customizedSceneCaseJobRepository =
        new CustomizedSceneCaseJobRepositoryImpl(mongoTemplate);

    private final static String MOCK_ID = "1";
    private final static Long COUNT = 1L;

    @Test
    @DisplayName("Test the page method in the CustomizedSceneCaseJobRepository")
    void page_test() {
        List<SceneCaseJob> sceneCaseJobList = Lists.newArrayList(SceneCaseJob.builder().id(MOCK_ID).build());
        when(mongoTemplate.find(any(Query.class), eq(SceneCaseJob.class))).thenReturn(sceneCaseJobList);
        when(mongoTemplate.count(any(Query.class), eq(SceneCaseJob.class))).thenReturn(COUNT);
        Page<SceneCaseJob> page = customizedSceneCaseJobRepository.page(Lists.newArrayList(MOCK_ID),
            PageDto.builder().build());
        assertThat(page).isNotNull();
    }

}
