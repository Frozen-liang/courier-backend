package com.sms.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.courier.entity.job.common.JobDataCollection;
import com.sms.courier.entity.job.common.JobEnvironment;
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
public class SceneCaseJobResponse extends BaseResponse {

    private String id;

    private String workspaceId;

    private String projectId;

    @JsonProperty("isNext")
    private boolean next;

    private List<JobSceneCaseApiResponse> apiTestCase;

    private JobEnvironment environment;

    private JobDataCollection dataCollection;

    private Integer jobStatus;

    private String message;
    /**
     * 测试人员.
     */
    private String createUserName;

}
