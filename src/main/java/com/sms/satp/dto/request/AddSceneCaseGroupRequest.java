package com.sms.satp.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddSceneCaseGroupRequest {

    @NotNull(message = "The projectId can not be empty")
    private String projectId;
    @NotNull(message = "The name can not be empty")
    private String name;

    private String parentId;
}
