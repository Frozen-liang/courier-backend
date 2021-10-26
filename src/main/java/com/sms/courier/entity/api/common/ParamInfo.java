package com.sms.courier.entity.api.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sms.courier.common.enums.ParamType;
import com.sms.courier.entity.structure.StructureEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.DBRef;
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
    @JsonIgnore
    private String value;
    /**
     * 字段描述.
     */
    @JsonIgnore
    private String description;
    /**
     * 字段类型.
     */
    private ParamType paramType;
    /**
     * 是否必填.
     */
    @Field("isRequired")
    private Boolean required;
    /**
     * 是否提取.
     */
    @Field("isExtract")
    @Default
    @JsonIgnore
    private boolean extract = true;
    /**
     * 对象子属性. JSON/Object/JsonArray.
     */
    @ToString.Exclude
    private List<ParamInfo> childParam;

    @ToString.Exclude
    @JsonIgnore
    private List<ParamInfo> customStructs;

    // 是否引用数据结构
    @Field("isRef")
    @JsonIgnore
    private boolean ref;

    /**
     * 自定义结构 用于struct.
     */
    @DBRef
    @JsonIgnore
    private StructureEntity structureRef;

    /**
     * 在数据结构中是唯一的.
     */
    @JsonIgnore
    private String paramId;

    /**
     * mock 参数生成规则.
     */
    @JsonIgnore
    private String mockRule;

    /**
     * mock api xml 属性.
     */
    @JsonIgnore
    private String attribute;

    /**
     * 用于前端显示更多操作.
     */
    @JsonIgnore
    private Integer structConsumer;
}