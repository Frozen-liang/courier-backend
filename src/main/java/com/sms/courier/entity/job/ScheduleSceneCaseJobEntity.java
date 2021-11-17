package com.sms.courier.entity.job;

import com.sms.courier.entity.job.common.AbstractSceneCaseJobEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "ScheduleSceneCaseJob")
public class ScheduleSceneCaseJobEntity extends AbstractSceneCaseJobEntity {

    @Field(targetType = FieldType.OBJECT_ID)
    private String scheduleRecordId;
}
