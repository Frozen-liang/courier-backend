package com.sms.courier.generator.pojo;

import com.sms.courier.common.enums.ParamType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeEntityParamVo {

    private String key;

    private String description;

    private String paramType;

    private ParamType oldParamType;

    private Boolean required;

    private List<CodeEntityParamVo> childParam;

}
