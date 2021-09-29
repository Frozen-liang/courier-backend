package com.sms.courier.entity.scenetest;

import com.sms.courier.common.enums.ApiType;
import com.sms.courier.entity.BaseEntity;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Document(collection = "CaseTemplateApi")
public class CaseTemplateApiEntity extends BaseEntity {

    @Field(targetType = FieldType.OBJECT_ID)
    private String caseTemplateId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    private ApiType apiType;

    private String shell;

    private Integer order;

    /**
     * 是否强制执行该步骤，即使其他步骤出错时.
     */
    @Field("isLock")
    private boolean lock;

    private String aliasName;

    private ApiTestCaseEntity apiTestCase;
}
