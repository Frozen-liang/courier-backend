package com.sms.satp.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.satp.dto.request.LogPageRequest;
import com.sms.satp.entity.log.LogEntity;
import com.sms.satp.repository.impl.CustomizedLogRepositoryImpl;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

@DisplayName("Tests for CustomizedLogRepository")
class CustomizedLogRepositoryTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final CustomizedLogRepository customizedLogRepository = new CustomizedLogRepositoryImpl(mongoTemplate);
    private static final Long TOTAL_ELEMENTS = 20L;

    @Test
    @DisplayName("Test the page method in the CustomizedLogRepository")
    public void page_test() {
        ArrayList<Object> logList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            logList.add(LogEntity.builder().build());
        }
        LogPageRequest logPageRequest = new LogPageRequest();
        logPageRequest.setQueryBeginTime(LocalDateTime.now());
        logPageRequest.setQueryEndTime(LocalDateTime.now());
        logPageRequest.setProjectId(ObjectId.get().toString());
        logPageRequest.setOperationDesc("desc");
        logPageRequest.setOperationType(0);
        logPageRequest.setOperationModule(0);
        logPageRequest.setOperator("test");
        logPageRequest.setOperatorId(2L);
        when(mongoTemplate.count(any(Query.class), any(Class.class))).thenReturn(TOTAL_ELEMENTS);
        when(mongoTemplate.find(any(Query.class), any())).thenReturn(logList);
        Page<LogEntity> page = customizedLogRepository.page(logPageRequest);
        assertThat(page.getTotalElements()).isEqualTo(TOTAL_ELEMENTS);
    }
}
