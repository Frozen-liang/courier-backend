package com.sms.satp.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApiTestCaseJobRunRequest {

    private List<String> apiTestCaseIds;

    private String envId;

    private String workspaceId;

    private DataCollectionRequest dataCollectionRequest;

}
