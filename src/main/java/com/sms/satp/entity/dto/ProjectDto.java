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
public class ProjectDto {

    private String id;
    @NotNull(message = "Project name cannot be empty")
    private String name;
    private String createDateTime;
    private String modifyDateTime;
}