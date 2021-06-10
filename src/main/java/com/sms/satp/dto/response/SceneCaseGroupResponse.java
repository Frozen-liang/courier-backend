package com.sms.satp.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SceneCaseGroupResponse {

    private String id;
    private String projectId;
    private String name;
    private String parentId;
    private Boolean removed;
    private Long createUserId;
    private Long modifyUserId;
    private LocalDateTime createDateTime;
    private LocalDateTime modifyDateTime;
}
