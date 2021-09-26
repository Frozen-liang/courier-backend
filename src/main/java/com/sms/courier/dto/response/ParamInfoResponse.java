package com.sms.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
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
     * 是否必填.
     */
    @Field("isRequired")
    @JsonProperty("isRequired")
    private Boolean required;
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
    @ToString.Exclude
    private List<ParamInfoResponse> childParam;

    @ToString.Exclude
    private List<ParamInfoResponse> customStructs;

    // 是否引用数据结构
    @JsonProperty("isRef")
    @Field("isRef")
    private boolean ref;

    private StructureRefResponse structureRef;

    private String paramId;

    private String mockRule;

    /**
     * 用于前端显示更多操作.
     */
    private Integer structConsumer;
}
