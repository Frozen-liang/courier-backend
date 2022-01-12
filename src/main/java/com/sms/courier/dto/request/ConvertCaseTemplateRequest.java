package com.sms.courier.dto.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ConvertCaseTemplateRequest {

    @NotNull(message = "The sceneCaseId can not be empty")
    private String sceneCaseId;

    @NotNull(message = "The groupId can not be empty")
    private String groupId;

    @NotNull(message = "The templateName can not be empty")
    private String templateName;

    @NotNull(message = "The projectId can not be empty")
    private String projectId;

    @NotNull(message = "The caseIds can not be null")
    @Size(min = 1, message = "The caseIds can not be empty")
    private List<String> caseIds;
}
