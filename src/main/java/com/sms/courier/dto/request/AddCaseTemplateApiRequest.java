package com.sms.courier.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.courier.common.enums.ApiBindingStatus;
import com.sms.courier.common.enums.ApiType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddCaseTemplateApiRequest {

    @NotNull(message = "The caseTemplateId can not be empty")
    private String caseTemplateId;

    private String projectId;

    private ApiType apiType;

    private String shell;

    private Integer order;

    @JsonProperty("isLock")
    private boolean lock;

    /**
     * API绑定状态.
     */
    private ApiBindingStatus apiBindingStatus;

    private ApiTestCaseRequest apiTestCase;
}
