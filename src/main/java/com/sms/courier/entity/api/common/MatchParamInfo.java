package com.sms.courier.entity.api.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.courier.common.enums.MatchType;
import com.sms.courier.common.enums.ParamType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchParamInfo {

    /**
     * 字段名.
     */
    private String key;
    /**
     * 字段值 eg. 数组[1,2,3,4,5] 字符串 abc Json字段值在childParam里面.
     */
    private String value;
    /**
     * 校验类型.
     */
    private MatchType matchType;
    /**
     * 字段类型.
     */
    private ParamType paramType;
    /**
     * 是否校验参数类型.
     */
    @Field("isVerificationParamType")
    @JsonProperty("isVerificationParamType")
    private boolean verificationParamType;
    /**
     * 是否校验数组内元素.
     */
    @Field("isVerificationArrayElement")
    @JsonProperty("isVerificationArrayElement")
    private boolean verificationArrayElement;
    /**
     * 是否校验.
     */
    @Field("isVerify")
    @JsonProperty("isVerify")
    private boolean verify;
    /**
     * 对象子属性. JSON/Object/JsonArray.
     */
    @ToString.Exclude
    private List<MatchParamInfo> childParam;
}
