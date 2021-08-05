package com.sms.courier.entity.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sms.courier.common.enums.ApiJsonType;
import com.sms.courier.common.enums.ApiProtocol;
import com.sms.courier.common.enums.ApiRequestParamType;
import com.sms.courier.common.enums.ApiStatus;
import com.sms.courier.common.enums.RawType;
import com.sms.courier.common.enums.RequestMethod;
import com.sms.courier.entity.BaseEntity;
import com.sms.courier.entity.api.common.ParamInfo;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.HashIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;


@SuppressFBWarnings("EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@SuperBuilder
@Document(collection = "Api")
@CompoundIndex(def = "{'projectId': 1, 'swaggerId': 1}")
public class ApiEntity extends BaseEntity {


    @JsonIgnore
    @HashIndexed
    @Indexed(background = true)
    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    @Field(targetType = FieldType.OBJECT_ID)
    @Indexed(background = true)
    @JsonIgnore
    private String groupId;

    @JsonIgnore
    @Field(targetType = FieldType.OBJECT_ID)
    private List<String> tagId;

    private String apiName;

    private String description;

    private String apiPath;

    private ApiProtocol apiProtocol;

    private RequestMethod requestMethod;

    private ApiRequestParamType apiRequestParamType;

    private String requestRaw;

    private RawType requestRawType;

    private List<ParamInfo> requestHeaders;

    private List<ParamInfo> responseHeaders;

    private List<ParamInfo> pathParams;

    private List<ParamInfo> restfulParams;

    private List<ParamInfo> requestParams;
    private List<ParamInfo> responseParams;
    @JsonIgnore
    private ApiStatus apiStatus;
    @JsonIgnore
    private String preInject;
    @JsonIgnore
    private String postInject;

    private String swaggerId;


    @JsonIgnore
    @Include
    private String md5;

    private ApiJsonType apiResponseJsonType;

    private ApiJsonType apiRequestJsonType;

    @JsonIgnore
    private ApiRequestParamType apiResponseParamType;

    private String responseRaw;

    private RawType responseRawType;

}
