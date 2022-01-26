package com.sms.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneratorTemplateResponse extends BaseResponse {

    private String name;

    private String projectId;

    private Integer codeType;

    @JsonProperty("isDefaultTemplate")
    private boolean defaultTemplate;

    private List<CodeTemplateResponse> codeTemplates;

}
