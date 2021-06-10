package com.sms.satp.dto.response;

import com.sms.satp.entity.job.JobCaseApi;
import com.sms.satp.entity.job.common.JobDataCollection;
import com.sms.satp.entity.job.common.JobEnvironment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiTestCaseJobResponse {

    private String id;

    private JobCaseApi apiTestCase;

    private JobEnvironment environment;

    private JobDataCollection dataCollection;

    private Integer jobStatus;

    private String message;
    /**
     * 测试人员.
     */
    private String createUserName;

    private Long createUserId;

    private Long modifyUserId;

    private String createDateTime;

    private String modifyDateTime;
}
