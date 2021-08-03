package com.sms.courier.dto.response;

import com.sms.courier.entity.job.common.JobDataCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ApiTestCaseJobResponse extends BaseResponse {

    private JobCaseApiResponse apiTestCase;

    private String workspaceId;

    private String projectId;

    private JobEnvironmentResponse environment;

    private JobDataCollection dataCollection;

    private Integer jobStatus;

    private String message;
    /**
     * 测试人员.
     */
    private String createUserName;

    private Integer time;

}
