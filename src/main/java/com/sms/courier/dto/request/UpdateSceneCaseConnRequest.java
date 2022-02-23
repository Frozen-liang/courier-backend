package com.sms.courier.dto.request;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSceneCaseConnRequest {

    @NotNull(message = "The projectId can not be empty")
    private String sceneCaseId;

    @NotNull(message = "The projectId can not be empty")
    private String sceneCaseName;

    @NotNull(message = "The projectId can not be empty")
    private String projectId;

    @Valid
    @NotNull(message = "The sceneCaseApiRequest can not be empty")
    @Size(min = 1, message = "The sceneCaseApiRequest cannot be empty")
    private List<UpdateSceneCaseApiConnRequest> sceneCaseApiRequest;
}
