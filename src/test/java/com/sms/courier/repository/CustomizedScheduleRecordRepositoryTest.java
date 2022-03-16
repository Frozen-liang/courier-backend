package com.sms.courier.repository;

import com.sms.courier.entity.job.ScheduleSceneCaseJobEntity;
import com.sms.courier.entity.schedule.ScheduleRecordEntity;
import com.sms.courier.repository.impl.CustomizedScheduleRecordRepositoryImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Tests for CustomizedScheduleRecordRepositoryTest")
public class CustomizedScheduleRecordRepositoryTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final CustomizedScheduleRecordRepository recordRepository =
        new CustomizedScheduleRecordRepositoryImpl(mongoTemplate);
    private static final String ID = new ObjectId().toString();

    @Test
    public void findAndModifyExecuteRecord_test() {
        ScheduleRecordEntity entity = ScheduleRecordEntity.builder().build();
        when(mongoTemplate.findAndModify(any(), any(), any(FindAndModifyOptions.class),
            any(Class.class))).thenReturn(entity);
        ScheduleRecordEntity dto = recordRepository.findAndModifyExecuteRecord(ID,
            ScheduleSceneCaseJobEntity.builder().build());
        assertThat(dto).isNotNull();
    }

}
