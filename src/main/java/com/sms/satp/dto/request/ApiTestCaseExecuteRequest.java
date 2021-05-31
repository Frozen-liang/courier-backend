package com.sms.satp.dto.request;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApiTestCaseExecuteRequest {

    @NotEmpty(message = "The apiTestCaseId must not empty.")
    private String apiTestCaseId;

    @NotEmpty(message = "The envId must not empty.")
    private String envId;

    private DataCollectionRequest dataCollectionRequest;

}
