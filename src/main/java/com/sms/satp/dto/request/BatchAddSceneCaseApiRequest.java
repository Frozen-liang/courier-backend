package com.sms.satp.dto.request;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BatchAddSceneCaseApiRequest {

    @Valid
    @NotEmpty(message = "The entity can not be empty")
    private List<AddSceneCaseApiRequest> addSceneCaseApiRequestList;
}
