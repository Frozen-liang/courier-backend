package com.sms.courier.dto.response;

import com.sms.courier.entity.generator.CodeFieldDesc;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneratorTemplateTypeResponse {

    private String name;

    private Integer templateType;

    private List<CodeFieldDesc> fieldDescList;
}
