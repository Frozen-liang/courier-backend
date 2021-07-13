package com.sms.satp.dto.request;

import com.sms.satp.common.enums.ApiBindingStatus;
import com.sms.satp.common.enums.ApiType;
import javax.crypto.Mac;
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

    /**
     * API绑定状态.
     */
    private ApiBindingStatus apiBindingStatus;

    private ApiTestCaseRequest apiTestCase;
}
