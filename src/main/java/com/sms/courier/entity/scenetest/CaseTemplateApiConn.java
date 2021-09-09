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

    /**
     * 是否强制执行该步骤，即使其他步骤出错时.
     */
    @Field("isLock")
    private boolean lock;
}
