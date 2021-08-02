package com.sms.satp.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.satp.common.enums.ParamType;
import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The key must not be empty.")
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

    private ParamType paramType;

    /**
     * 是否必填.
     */
    @Field("isRequired")
    @JsonProperty("isRequired")
    private boolean required;
    /**
     * 对象子属性. JSON/Object/JsonArray.
     */
    @Builder.Default
    @ToString.Exclude
    @Valid
    private List<ParamInfoRequest> childParam = new ArrayList<>();
}
