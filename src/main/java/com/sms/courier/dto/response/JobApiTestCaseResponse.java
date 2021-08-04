package com.sms.courier.dto.response;

import com.sms.courier.entity.api.common.AdvancedSetting;
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
