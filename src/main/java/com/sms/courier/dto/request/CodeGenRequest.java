package com.sms.courier.dto.request;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeGenRequest {

    @NotNull(message = "The apiId can not be empty")
    private String apiId;

    private String packageName;

    @NotNull(message = "The templateId can not be empty")
    private String templateId;

    @NotEmpty(message = "The templateType can not be empty")
    private List<Integer> templateType;

}
