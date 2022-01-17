package com.sms.courier.dto.request;

import com.sms.courier.entity.generator.CodeTemplate;
import com.sms.courier.generator.enums.CodeType;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddGeneratorTemplateRequest {
    @NotNull(message = "The name can not be empty")
    private String name;
    @NotNull(message = "The codeType can not be empty")
    private CodeType codeType;
    private List<CodeTemplate> codeTemplates;
}
