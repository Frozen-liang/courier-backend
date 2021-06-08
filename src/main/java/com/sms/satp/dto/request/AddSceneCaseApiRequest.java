package com.sms.satp.dto.request;

import com.sms.satp.common.enums.ApiBindingStatus;
import com.sms.satp.common.enums.ApiType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddSceneCaseApiRequest {

    private String sceneCaseId;

    private String projectId;

    private ApiType apiType;

    private String shell;

    private Integer order;

    /**
     * API绑定状态.
     */
    private ApiBindingStatus apiBindingStatus;

    private ApiTestCaseRequest apiTestCaseRequest;
}
