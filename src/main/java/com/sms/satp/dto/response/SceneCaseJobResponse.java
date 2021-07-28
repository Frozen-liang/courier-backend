package com.sms.satp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.satp.entity.job.common.JobDataCollection;
import com.sms.satp.entity.job.common.JobEnvironment;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SceneCaseJobResponse {

    private String id;

    private String workspaceId;

    private String projectId;

    @JsonProperty("isLock")
    private boolean lock;

    private List<JobSceneCaseApiResponse> apiTestCase;

    private JobEnvironment environment;

    private JobDataCollection dataCollection;

    private Integer jobStatus;

    private String message;
    /**
     * 测试人员.
     */
    private String createUserName;

    private String createUserId;

    private String modifyUserId;

    private String createDateTime;

    private String modifyDateTime;
}
