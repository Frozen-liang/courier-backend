package com.sms.satp.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class LogResponse {

    private String id;

    private String projectId;

    private Integer operationType;

    private Integer operationModule;

    private String operationDesc;

    private String operator;

    private Long operatorId;

    private LocalDateTime createDateTime;
}
