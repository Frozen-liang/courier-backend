package com.sms.satp.entity.job.common;

import com.sms.satp.dto.response.ParamInfoResponse;
import com.sms.satp.dto.response.ResponseHeadersVerificationResponse;
import com.sms.satp.dto.response.ResponseResultVerificationResponse;
import com.sms.satp.entity.api.common.HttpStatusVerification;
import com.sms.satp.entity.api.common.ResponseTimeVerification;
import java.util.List;
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
public class JobApiTestCase {

    @Field(targetType = FieldType.OBJECT_ID)
    private String id;

    @Field(targetType = FieldType.OBJECT_ID)
    private String apiId;

    private String caseName;

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    private List<String> tagIds;

    private String apiName;

    private String description;

    private String apiPath;

    private Integer apiProtocol;

    private Integer requestMethod;

    private Integer apiRequestParamType;

    private List<ParamInfoResponse> requestHeaders;
    private List<ParamInfoResponse> pathParams;
    private List<ParamInfoResponse> restfulParams;
    private List<ParamInfoResponse> requestParams;
    private List<ParamInfoResponse> responseParams;

    private Integer responseParamsExtractionType;

    private String preInject;

    private String postInject;

    private Integer apiResponseJsonType;

    private Integer apiRequestJsonType;

    private HttpStatusVerification httpStatusVerification;

    private ResponseHeadersVerificationResponse responseHeadersVerification;

    private ResponseResultVerificationResponse responseResultVerification;

    private ResponseTimeVerification responseTimeVerification;

    private boolean execute;

    private Long modifyUserId;

    private String modifyDateTime;

    private CaseReport caseReport;

}
