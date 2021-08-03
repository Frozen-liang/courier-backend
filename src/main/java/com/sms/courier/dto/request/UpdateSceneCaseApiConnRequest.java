package com.sms.courier.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.courier.dto.response.CaseTemplateApiResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSceneCaseApiConnRequest {

    private String id;

    private String sceneCaseId;

    private String caseTemplateId;

    private Integer order;

    @JsonProperty("isLock")
    private boolean lock;

    private ApiTestCaseRequest apiTestCase;

    private List<CaseTemplateApiResponse> caseTemplateApiList;

}
