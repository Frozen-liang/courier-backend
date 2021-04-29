package com.sms.satp.dto;

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
public class SceneCaseDto {

    private String id;
    private String name;
    private String createUserName;
    private String projectId;
    private String groupId;
    private String testStatus;
    private List<String> caseTag;
    private Integer priority;
    private boolean remove;
    private LocalDateTime createDateTime;
    private LocalDateTime modifyDateTime;
}
