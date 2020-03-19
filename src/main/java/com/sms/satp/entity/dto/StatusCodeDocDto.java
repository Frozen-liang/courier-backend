package com.sms.satp.entity.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusCodeDocDto {

    private String id;
    @NotNull(message = "StatusCode cannot be empty")
    private String code;
    private String description;
    @NotNull(message = "ProjectId cannot be empty")
    private String projectId;
    private String createDateTime;
    private String modifyDateTime;

}