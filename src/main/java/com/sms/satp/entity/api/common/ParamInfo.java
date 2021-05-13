package com.sms.satp.entity.api.common;

import com.sms.satp.common.enums.ParamType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamInfo {

    /**
     * 字段名.
     */
    @Include
    private String key;
    /**
     * 字段值 eg. 数组[1,2,3,4,5] 字符串 abc Json字段值在childParam里面.
     */
    @Include
    private String value;
    /**
     * 字段描述.
     */
    @Include
    private String description;
    /**
     * 字段类型.
     */
    @Include
    private ParamType paramType;
    /**
     * 是否递归引用自己.
     */
    @Include
    private Boolean reference;
    /**
     * 是否必填.
     */
    @Include
    private Boolean required;
    /**
     * 对象子属性. JSON/Object/JsonArray.
     */
    @ToString.Exclude
    @Include
    private List<ParamInfo> childParam;
}