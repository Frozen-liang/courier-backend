package com.sms.satp.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseTemplateResponse {

    private String id;
    private String name;
    private String createUserName;
    private String projectId;
    private String groupId;
    private String testStatus;
    private List<String> caseTag;
    private boolean remove;
    private String createUserId;
    private LocalDateTime createDateTime;
    private String modifyUserId;
    private LocalDateTime modifyDateTime;
}
