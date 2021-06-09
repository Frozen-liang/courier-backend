package com.sms.satp.dto.response;

import com.sms.satp.entity.job.common.CaseReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiTestCaseJobPageResponse {

    private String testDateTime;
    private String testUser;
    private CaseReport testReport;
}
