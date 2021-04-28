package com.sms.satp.entity.scenetest;

import com.sms.satp.common.enums.ApiProtocol;
import com.sms.satp.common.enums.ApiRequestParamType;
import com.sms.satp.common.enums.RequestMethod;
import com.sms.satp.entity.api.common.ParamInfo;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
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

    private List<ParamInfo> requestBody;

    private List<ParamInfo> requestHeaders;

    private List<ParamInfo> queryParams;

    private List<ParamInfo> pathParams;

    private String preInject;

    private String postInject;

    private ApiRequestParamType apiResponseParamType;

    private List<ParamInfo> responseHeaders;

    private List<ParamInfo> responseParams;

    private String matchRule;

    private Integer orderNumber;

    private Integer isExecute;

    private boolean remove;
    @CreatedBy
    private Long createUserId;
    @CreatedDate
    private LocalDateTime createDateTime;
    @LastModifiedBy
    private Long modifyUserId;
    @LastModifiedDate
    private LocalDateTime modifyDateTime;
}
