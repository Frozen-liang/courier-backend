package com.sms.satp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobSceneCaseApiResponse {

    private String id;

    private String sceneCaseId;

    private String caseTemplateId;

    private Integer apiType;

    private String shell;

    private Integer order;

    private JobApiTestCaseResponse jobApiTestCase;
}
