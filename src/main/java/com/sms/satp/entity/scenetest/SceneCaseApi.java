package com.sms.satp.entity.scenetest;

import com.sms.satp.common.enums.ApiProtocol;
import com.sms.satp.common.enums.ApiRequestParamType;
import com.sms.satp.common.enums.RequestMethod;
import com.sms.satp.entity.test.CaseHeader;
import com.sms.satp.entity.test.CaseParameter;
import com.sms.satp.entity.test.CaseRequestBody;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "SceneCaseApi")
public class SceneCaseApi {

    @MongoId(value = FieldType.OBJECT_ID)
    private String id;

    @Field(targetType = FieldType.OBJECT_ID)
    private String apiId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String sceneCaseId;

    private String apiName;

    private String apiUrl;

    private ApiProtocol apiProtocol;

    private RequestMethod requestMethod;

    private ApiRequestParamType apiRequestParamType;

    private CaseRequestBody requestBody;

    private List<CaseHeader> requestHeaders;

    private List<CaseParameter> queryParams;

    private List<CaseParameter> pathParams;

    private String preInject;

    private String postInject;

    private ApiRequestParamType apiResponseParamType;

    private List<CaseHeader> responseHeaders;

    private List<CaseParameter> responseParams;

    private String matchRule;

    private Integer orderNumber;

    private Integer isExecute;

    private Integer status;

    private String createUserId;

    @CreatedDate
    private LocalDateTime createDateTime;

    private String modifyUserId;

    @LastModifiedDate
    private LocalDateTime modifyDateTime;
}
