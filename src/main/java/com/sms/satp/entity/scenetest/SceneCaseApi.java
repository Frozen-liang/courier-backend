package com.sms.satp.entity.scenetest;

import com.sms.satp.common.enums.ApiJsonType;
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

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    private String apiName;

    private String description;

    private String apiPath;

    private ApiProtocol apiProtocol;

    private RequestMethod requestMethod;

    private ApiRequestParamType apiRequestParamType;

    private List<ParamInfo> requestHeaders;
    private List<ParamInfo> responseHeaders;
    private List<ParamInfo> pathParams;
    private List<ParamInfo> restfulParams;
    private List<ParamInfo> requestParams;
    private List<ParamInfo> responseParams;

    private String preInject;

    private String postInject;

    private ApiJsonType apiResponseJsonType;

    private ApiJsonType apiRequestJsonType;

    private String matchRule;

    private Integer timeoutLimit;

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
