package com.sms.courier.entity.api.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sms.courier.common.enums.ApiEncodingType;
import com.sms.courier.common.enums.ApiJsonType;
import com.sms.courier.common.enums.ApiNodeType;
import com.sms.courier.common.enums.ApiProtocol;
import com.sms.courier.common.enums.ApiRequestParamType;
import com.sms.courier.common.enums.ApiStatus;
import com.sms.courier.common.enums.RawType;
import com.sms.courier.common.enums.RequestMethod;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiHistoryDetail {

    private String id;

    @Field(name = "isRemove")
    private boolean remove;

    @Field(targetType = FieldType.OBJECT_ID)
    private String createUserId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String modifyUserId;

    private LocalDateTime createDateTime;

    private LocalDateTime modifyDateTime;

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String groupId;

    @Field(targetType = FieldType.OBJECT_ID)
    private List<String> tagId;

    private String apiName;

    private String description;

    private String apiPath;

    private ApiProtocol apiProtocol;

    private RequestMethod requestMethod;

    private String requestRaw;

    private RawType requestRawType;

    private ApiRequestParamType apiRequestParamType;

    private List<ParamInfo> requestHeaders;

    private List<ParamInfo> responseHeaders;

    private List<ParamInfo> pathParams;

    private List<ParamInfo> restfulParams;

    private List<ParamInfo> requestParams;
    private List<ParamInfo> responseParams;

    private ApiStatus apiStatus;

    private String preInject;

    private String postInject;

    private String swaggerId;

    private String md5;

    private ApiJsonType apiResponseJsonType;

    private ApiJsonType apiRequestJsonType;

    private ApiRequestParamType apiResponseParamType;

    private String responseRaw;

    private RawType responseRawType;

    @Field(targetType = FieldType.OBJECT_ID)
    @JsonIgnore
    private String apiManagerId;

    private String richText;

    private String markdown;

    private ApiNodeType apiNodeType;

    private ApiEncodingType apiEncodingType;
}
