package com.sms.satp.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiGroupResponse {

    private String id;

    private String projectId;

    private String name;

    private Boolean removed;

    private Long createUserId;

    private Long modifyUserId;

    private LocalDateTime createDateTime;

    private LocalDateTime modifyDateTime;
}
