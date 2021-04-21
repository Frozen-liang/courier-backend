package com.sms.satp.entity.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddSceneCaseDto {

    @NotNull(message = "The name can not be empty")
    private String name;
    @NotNull(message = "The projectId can not be empty")
    private String projectId;
    @NotNull(message = "The groupId can not be empty")
    private String groupId;
    private List<String> caseTag;
    private int priority;
    @NotNull(message = "The createUserId can not be empty")
    private String createUserId;

}
