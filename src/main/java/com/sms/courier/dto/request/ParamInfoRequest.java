package com.sms.courier.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParamInfoRequest {

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
     * 是否必填.
     */
    @Field("isRequired")
    @JsonProperty("isRequired")
    private boolean required;
    /**
     * 是否提取.
     */
    @Field("isExtract")
    @JsonProperty("isExtract")
    @Default
    private boolean extract = true;
    /**
     * 对象子属性. JSON/Object/JsonArray.
     */
    @Builder.Default
    @ToString.Exclude
    @Valid
    private List<ParamInfoRequest> childParam = new ArrayList<>();

    @ToString.Exclude
    private List<ParamInfoRequest> customStructs;

    // 是否引用数据结构
    @Field("isRef")
    @JsonProperty("isRef")
    private boolean ref;

    /**
     * 自定义结构 用于struct.
     */
    private IdRequest structureRef;

    private String paramId;
}
