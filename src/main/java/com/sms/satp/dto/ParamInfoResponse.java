package com.sms.satp.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParamInfoResponse {

    /**
     * 字段名.
     */
    private String key;
    /**
     * 字段值 eg. 数组[1,2,3,4,5] 字符串 abc Json字段值在childParam里面.
     */
    private String value;
    /**
     * 字段描述.
     */
    private String description;
    /**
     * 字段类型.
     *
     * @link ParamType
     */
    private Integer paramType;
    /**
     * 是否递归引用自己.
     */
    private Boolean reference;
    /**
     * 是否必填.
     */
    private Boolean required;
    /**
     * 对象子属性. JSON/Object/JsonArray.
     */
    @Builder.Default
    @ToString.Exclude
    private List<ParamInfoResponse> childParam = new ArrayList<>();
}
