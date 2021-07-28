package com.sms.satp.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    private String workspaceId;

    private String caseTemplateId;

    private String projectId;

    @JsonProperty("isLock")
    private boolean lock;

    private DataCollectionRequest dataCollectionRequest;

    private String envId;
}
