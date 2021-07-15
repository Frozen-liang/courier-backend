package com.sms.satp.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SceneCaseApiConnResponse extends BaseResponse {

    private String sceneCaseId;

    private String caseTemplateId;

    private String projectId;

    private Integer apiType;

    private String shell;

    private Integer order;

    private ApiTestCaseResponse apiTestCase;

    private List<CaseTemplateApiResponse> caseTemplateApiResponseList;
}