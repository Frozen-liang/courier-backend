package com.sms.satp.dto.request;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddSceneCaseApiByIdsRequest {

    @NotNull(message = "The sceneCaseId can not be empty")
    private String sceneCaseId;

    @NotEmpty(message = "The entity can not be empty")
    private List<AddSceneCaseApi> sceneCaseApis;
}
