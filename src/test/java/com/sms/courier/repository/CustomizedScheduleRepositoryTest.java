package com.sms.courier.repository;

import com.google.common.collect.Lists;
import com.mongodb.client.result.UpdateResult;
import com.sms.courier.entity.schedule.ScheduleEntity;
import com.sms.courier.repository.impl.CustomizedScheduleRepositoryImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Tests for CustomizedScheduleRepositoryTest")
public class CustomizedScheduleRepositoryTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private CustomizedScheduleRepository customizedScheduleRepository =
        new CustomizedScheduleRepositoryImpl(mongoTemplate);
    private static final String ID = new ObjectId().toString();

    @Test
    @DisplayName("Test the removeCaseIds method in the CustomizedScheduleRepository")
    public void removeCaseIds_test() {
        UpdateResult result = mock(UpdateResult.class);
        when(mongoTemplate.updateMulti(any(), any(), eq(ScheduleEntity.class)))
            .thenReturn(result);
        Boolean isSuccess = customizedScheduleRepository.removeCaseIds(Lists.newArrayList(ID));
        assertTrue(isSuccess);
    }

}
