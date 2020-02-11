package com.sms.satp.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectEnvironmentDto {

    private String id;
    private String name;
    private String desc;
    private String projectId;
    private String basePath;
    private String createDateTime;
    private String modifyDateTime;
}