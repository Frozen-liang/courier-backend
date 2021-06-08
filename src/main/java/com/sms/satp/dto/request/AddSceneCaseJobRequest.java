package com.sms.satp.dto.request;

import com.sms.satp.entity.datacollection.DataCollection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddSceneCaseJobRequest {

    private String sceneCaseId;

    @NotNull(message = "The projectId can not be empty")
    private String projectId;

    private List<String> sceneCaseApiIds;

    private List<String> caseTemplateConnIds;

    private DataCollection dataCollection;

    private String envId;
}
