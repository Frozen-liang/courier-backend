package com.sms.courier.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncApiRequest {

    @NotNull(message = "The caseId cannot be empty")
    private String caseId;
    @NotNull(message = "The caseName cannot be empty")
    private String caseName;
    @NotNull(message = "The projectId cannot be empty")
    private String projectId;
}
