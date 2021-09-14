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
public class AddCaseTemplateApiByIdsRequest {

    @NotNull(message = "The caseTemplateId can not be empty")
    private String caseTemplateId;

    @NotNull(message = "The projectId can not be empty")
    private String projectId;

    @NotEmpty(message = "The entity can not be empty")
    private List<AddSceneCaseApi> caseTemplateApis;
}
