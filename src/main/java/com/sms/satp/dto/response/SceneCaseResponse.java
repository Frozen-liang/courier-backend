package com.sms.satp.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SceneCaseResponse {

    private String id;
    private String name;
    private String createUserName;
    private String projectId;
    private String groupId;
    private String testStatus;
    private List<String> tagIds;
    private Integer priority;
    private Boolean removed;
    private LocalDateTime createDateTime;
    private LocalDateTime modifyDateTime;
}
