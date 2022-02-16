package com.sms.courier.generator;

import com.sms.courier.entity.generator.CodeTemplate;

public interface TemplateEngine {

    String getRendered(BaseCodegen codegenModel, CodeTemplate codeTemplate);
}
