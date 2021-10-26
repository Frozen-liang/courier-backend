package com.sms.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobSceneCaseApiResponse {

    private String caseId;

    private String id;

    private String sceneCaseId;

    private String caseTemplateId;

    private Integer apiType;

    private String shell;

    private Integer order;

    @JsonProperty("isLock")
    private boolean lock;

    private JobApiTestCaseResponse jobApiTestCase;

}
