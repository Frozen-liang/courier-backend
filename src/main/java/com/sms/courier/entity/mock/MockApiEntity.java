package com.sms.courier.entity.mock;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sms.courier.common.enums.ApiJsonType;
import com.sms.courier.common.enums.ApiRequestParamType;
import com.sms.courier.common.enums.MockApiResponseParamType;
import com.sms.courier.common.enums.RawType;
import com.sms.courier.entity.BaseEntity;
import com.sms.courier.entity.api.common.MatchParamInfo;
import com.sms.courier.entity.api.common.ParamInfo;
import com.sms.courier.utils.DurationToStringSerializer;
import com.sms.courier.utils.StringToDurationSerializer;
import java.time.Duration;
import java.util.List;
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
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Document(collection = "MockApi")
public class MockApiEntity extends BaseEntity {

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String apiId;

    private String mockName;

    private String mockPath;

    /**
     * 根数据生成规则.
     */
    private String rootMockRule;

    private MockApiResponseParamType apiResponseParamType;

    private ApiRequestParamType apiRequestParamType;

    private String requestRaw;

    private RawType requestRawType;

    private List<ParamInfo> requestHeaders;

    private List<ParamInfo> responseHeaders;

    private List<ParamInfo> pathParams;

    private List<ParamInfo> restfulParams;

    private List<MatchParamInfo> requestParams;

    private List<ParamInfo> responseParams;

    private String responseRaw;

    private RawType responseRawType;

    private ApiJsonType apiResponseJsonType;

    private ApiJsonType apiRequestJsonType;

    /**
     * xml 声明.
     */
    private String xmlDeclaration;

    private String statusCode;

    /**
     * 延期时间，单位：毫秒.
     */
    @JsonSerialize(using = DurationToStringSerializer.class)
    @JsonDeserialize(using = StringToDurationSerializer.class)
    private Duration delayTime;

    @Field(value = "isEnable")
    private boolean enable;

}
