package com.sms.satp.entity.scenetest;

import com.sms.satp.common.enums.ApiBindingStatus;
import com.sms.satp.common.enums.ApiType;
import com.sms.satp.entity.BaseEntity;
import com.sms.satp.entity.apitestcase.ApiTestCase;
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
public class CaseTemplateApi extends BaseEntity {

    @Field(targetType = FieldType.OBJECT_ID)
    private String caseTemplateId;

    private ApiType apiType;

    private String shell;

    private Integer order;

    /**
     * API绑定状态.
     */
    private ApiBindingStatus apiBindingStatus;

    private ApiTestCase apiTestCase;
}
