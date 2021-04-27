package com.sms.satp.entity.dto;

import com.sms.satp.common.enums.OperationType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SceneCaseApiLogDto {

    private String projectId;

    private String sceneCaseId;

    private String sceneCaseApiId;

    private String templateCaseApiId;

    private OperationType operationType;

    private String operationTarget;

    private String operationUserName;

    private Long createUserId;

    private LocalDateTime createDateTime;
}
