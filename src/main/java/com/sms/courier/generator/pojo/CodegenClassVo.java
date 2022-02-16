package com.sms.courier.generator.pojo;

import com.sms.courier.generator.BaseCodegen;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CodegenClassVo extends BaseCodegen {

    private String route;

    private String requestMethod;

    private String requestName;

    private String queryName;

    private String responseName;

    private String serviceName;

    private List<CodeEntityParamVo> pathParams;

    private String methodName;

}
