package com.sms.courier.dto.request;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CopyStepsRequest {

    @NotNull(message = "The sceneCaseId must not be null!")
    private String sceneCaseId;

    @NotNull(message = "The projectId must not be null!")
    private String projectId;

    @NotNull(message = "The caseOrderList must not be null!")
    @Size(min = 1, message = "The caseOrderList size must not be empty!")
    private List<CaseOrderRequest> caseOrderList;

    @Valid
    @Data
    @Builder
    public static class CaseOrderRequest {

        @NotNull(message = "The caseId must not be null!")
        private String caseId;

        @NotNull(message = "The caseName must not be null!")
        private String caseName;

        @NotNull(message = "The order must not be null!")
        private Integer order;
    }

}
