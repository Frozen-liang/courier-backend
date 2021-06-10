package com.sms.satp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {

    private String id;

    private String name;

    private String description;

    private String version;

    private Integer type;

    private String createDateTime;

    private String modifyDateTime;

    private Long createUserId;

    private Long modifyUserId;
}