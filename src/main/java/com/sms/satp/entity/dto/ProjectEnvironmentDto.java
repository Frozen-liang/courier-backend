package com.sms.satp.entity.dto;

import com.sms.satp.entity.AuthInfo;
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
    private String serverAddress;
    private AuthInfo authInfo;
    private String createDateTime;
    private String modifyDateTime;
}