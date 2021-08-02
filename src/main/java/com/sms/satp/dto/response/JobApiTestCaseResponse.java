package com.sms.satp.dto.response;

import com.sms.satp.entity.api.common.AdvancedSetting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobApiTestCaseResponse {

    private ApiTestCaseResponse apiTestCase;

    private AdvancedSetting advancedSetting;

    private CaseReportResponse caseReport;

}
