package com.sms.courier.dto.request;

import javax.validation.constraints.NotNull;
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

    @NotNull(message = "The sceneCaseName can not be empty")
    private String sceneCaseName;

    @NotNull(message = "The projectId can not be empty")
    private String projectId;
}
