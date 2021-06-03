package com.sms.satp.dto.request;

import com.sms.satp.entity.datacollection.DataCollection;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddSceneCaseJobRequest {

    @NotNull(message = "The sceneCaseId can not be empty")
    private String sceneCaseId;

    private List<String> sceneCaseApiIds;

    private List<String> caseTemplateConnIds;

    private DataCollection dataCollection;

    @NotNull(message = "The environmentId can not be empty")
    private String environmentId;
}
