package com.sms.courier.entity.apitestcase;

import com.sms.courier.common.enums.ApiBindingStatus;
import com.sms.courier.common.enums.ResponseParamsExtractionType;
import com.sms.courier.entity.BaseEntity;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.api.common.AdvancedSetting;
import com.sms.courier.entity.api.common.HttpStatusVerification;
import com.sms.courier.entity.api.common.ResponseHeadersVerification;
import com.sms.courier.entity.api.common.ResponseResultVerification;
import com.sms.courier.entity.api.common.ResponseTimeVerification;
import java.util.List;
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
@Document(collection = "ApiTestCase")
public class ApiTestCaseEntity extends BaseEntity {

    private String caseName;

    @Field(targetType = FieldType.OBJECT_ID)
    private String dataCollId;

    @Field(targetType = FieldType.OBJECT_ID)
    private List<String> tagId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    private ApiBindingStatus status;

    private ResponseParamsExtractionType responseParamsExtractionType;

    private HttpStatusVerification httpStatusVerification;

    private ResponseHeadersVerification responseHeadersVerification;

    private ResponseResultVerification responseResultVerification;

    private ResponseTimeVerification responseTimeVerification;

    @Field(name = "isExecute")
    private boolean execute;

    private AdvancedSetting advancedSetting;

    private ApiEntity apiEntity;

    private TestResult lastTestResult;
}
