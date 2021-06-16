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
public class SceneCaseApiResponse {

    private String id;
    private String sceneCaseId;
    private Integer apiType;
    private String shell;
    private Integer order;
    private Boolean removed;
    private ApiTestCaseResponse apiTestCaseResponse;
    private Long createUserId;
    private LocalDateTime createDateTime;
    private Long modifyUserId;
    private LocalDateTime modifyDateTime;
}
