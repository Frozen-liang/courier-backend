package com.sms.satp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApiTagGroupResponse {

    private String id;

    private String projectId;

    private String name;

    private String createDateTime;

    private String modifyDateTime;

    private Long createUserId;

    private Long modifyUserId;
}
