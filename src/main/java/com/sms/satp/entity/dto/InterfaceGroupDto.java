package com.sms.satp.entity.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterfaceGroupDto {

    private String id;
    @NotNull(message = "ProjectId cannot be empty")
    private String projectId;
    @NotNull(message = "GroupName cannot be empty")
    private String name;

}