package com.sms.satp.dto.request;

import com.sms.satp.dto.response.CaseTemplateApiResponse;
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

    private ApiTestCaseRequest apiTestCase;

    private List<CaseTemplateApiResponse> caseTemplateApiList;

}
