package com.sms.courier.entity.api.common;

import com.sms.courier.common.enums.ParamType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamInfo {

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
     */
    private ParamType paramType;
    /**
     * 是否必填.
     */
    @Field("isRequired")
    @Default
    private boolean required = false;
    /**
     * 是否提取.
     */
    @Field("isExtract")
    @Default
    private boolean extract = true;
    /**
     * 对象子属性. JSON/Object/JsonArray.
     */
    @ToString.Exclude
    private List<ParamInfo> childParam;
}