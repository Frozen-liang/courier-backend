package com.sms.courier.entity.job;

import com.sms.courier.entity.job.common.AbstractCaseJobEntity;
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
@Document(collection = "ScheduleCaseJob")
public class ScheduleCaseJobEntity extends AbstractCaseJobEntity {

    private String name;
    @Field(targetType = FieldType.OBJECT_ID)
    private String scheduleRecordId;
}
