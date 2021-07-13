package com.sms.satp.dto.response;

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
public class CaseTemplateApiResponse extends BaseResponse {

    private String caseTemplateId;
    private String projectId;
    private Integer apiType;
    private String shell;
    private Integer order;
    private ApiTestCaseResponse apiTestCase;
}
