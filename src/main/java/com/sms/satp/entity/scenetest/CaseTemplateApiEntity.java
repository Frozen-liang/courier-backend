package com.sms.satp.entity.scenetest;

import com.sms.satp.common.enums.ApiType;
import com.sms.satp.entity.BaseEntity;
import com.sms.satp.entity.apitestcase.ApiTestCaseEntity;
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

    private ApiTestCaseEntity apiTestCase;
}
