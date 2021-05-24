package com.sms.satp.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    private Boolean isVerificationParamType;
    /**
     * 是否校验数组内元素.
     */
    private Boolean isVerificationArrayElement;
    /**
     * 是否必含.
     */
    private Boolean required;
    /**
     * 对象子属性. JSON/Object/JsonArray.
     */
    @Builder.Default
    @ToString.Exclude
    private List<MatchParamInfoResponse> childParam = new ArrayList<>();
}
