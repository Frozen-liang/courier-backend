package com.sms.satp.dto.request;

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

    private String caseTemplateId;

    private String projectId;

    private DataCollectionRequest dataCollectionRequest;

    private String envId;
}
