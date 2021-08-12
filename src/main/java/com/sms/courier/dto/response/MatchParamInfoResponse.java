package com.sms.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
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
public class MatchParamInfoResponse {

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
    private Integer matchType;
    /**
     * 字段类型.
     */
    private Integer paramType;
    /**
     * 是否校验参数类型.
     */
    @JsonProperty("isVerificationParamType")
    @Field("isVerificationParamType")
    private boolean verificationParamType;
    /**
     * 是否校验数组内元素.
     */
    @JsonProperty("isVerificationArrayElement")
    @Field("isVerificationArrayElement")
    private boolean verificationArrayElement;
    /**
     * 是否校验.
     */
    @JsonProperty("isVerify")
    @Field("isVerify")
    private boolean verify;
    /**
     * 对象子属性. JSON/Object/JsonArray.
     */
    @Builder.Default
    @ToString.Exclude
    private List<MatchParamInfoResponse> childParam = new ArrayList<>();
}
