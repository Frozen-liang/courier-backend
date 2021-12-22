package com.sms.courier.entity.scenetest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnvDataCollConn {

    @Field(targetType = FieldType.OBJECT_ID)
    private String envId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String dataCollId;
}
