package com.sms.satp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class SceneCaseApiResponse extends BaseResponse {

    private String sceneCaseId;
    private String projectId;
    private Integer apiType;
    private String shell;
    private Integer order;
    @JsonProperty("isLock")
    private boolean lock;
    private ApiTestCaseResponse apiTestCase;
}
