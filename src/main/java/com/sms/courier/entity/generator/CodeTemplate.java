package com.sms.courier.entity.generator;

import com.sms.courier.common.enums.TemplateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeTemplate {

    private String name;

    private String value;

    private TemplateType templateType;

}
