package com.sms.satp.entity.api.common;

import com.sms.satp.common.enums.MatchType;
import com.sms.satp.common.enums.ParamType;
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
     * 校验结果.
     */
    private String verificationResult;
    /**
     * 对象子属性. JSON/Object/JsonArray.
     */
    @ToString.Exclude
    private List<MatchParamInfo> childParam;
}
