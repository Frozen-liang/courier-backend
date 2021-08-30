package com.sms.courier.entity.scenetest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseTemplateApiConn {

    @Field(targetType = FieldType.OBJECT_ID)
    private String caseTemplateApiId;

    @Field(name = "isExecute")
    private boolean execute;
}