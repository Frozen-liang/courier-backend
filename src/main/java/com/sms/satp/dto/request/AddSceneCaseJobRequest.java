package com.sms.satp.dto.request;

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

    private String projectId;

    private List<String> sceneCaseApiIds;

    private List<String> caseTemplateConnIds;

    private DataCollectionRequest dataCollectionRequest;

    private String envId;
}
