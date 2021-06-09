package com.sms.satp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApiTestCaseJobRunRequest {

    private String apiTestCaseId;

    private String envId;

    private DataCollectionRequest dataCollectionRequest;

}
