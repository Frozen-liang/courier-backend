package com.sms.satp.entity.api;

import com.sms.satp.common.enums.ApiJsonType;
import com.sms.satp.common.enums.ApiProtocol;
import com.sms.satp.common.enums.ApiRequestParamType;
import com.sms.satp.common.enums.ApiStatus;
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
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Api")
public class ApiEntity {

    @MongoId(FieldType.OBJECT_ID)
    @Indexed(unique = true)
    private String id;

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String groupId;

    @Field(targetType = FieldType.OBJECT_ID)
    private List<String> tagId;

    @Indexed(unique = true)
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

    private ApiStatus apiStatus;

    private boolean removed;

    private String preInject;

    private String postInject;

    private String swaggerId;

    private ApiJsonType apiResponseJsonType;
    private ApiJsonType apiRequestJsonType;

    @CreatedBy
    private Long createUserId;

    @CreatedDate
    private LocalDateTime createDateTime;
    @LastModifiedDate
    private LocalDateTime modifyDateTime;


}
