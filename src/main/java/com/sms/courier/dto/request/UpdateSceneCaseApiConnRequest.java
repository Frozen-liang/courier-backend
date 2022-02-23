package com.sms.courier.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.courier.dto.response.CaseTemplateApiResponse;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSceneCaseApiConnRequest {

    @NotNull(message = "The id can not be empty")
    private String id;

    private String sceneCaseId;

    private String caseTemplateId;

    @NotNull(message = "The order can not be empty")
    private Integer order;

    @NotNull(message = "The isLock can not be empty")
    @JsonProperty("isLock")
    private boolean lock;

    private String aliasName;

    private ApiTestCaseRequest apiTestCase;

    private List<CaseTemplateApiResponse> caseTemplateApiList;

}
