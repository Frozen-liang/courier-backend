package com.sms.satp.dto.response;

import static com.sms.satp.common.constant.TimePatternConstant.DEFAULT_PATTERN;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectImportFlowResponse {

    private String id;
    private String projectId;
    private String importSourceId;
    @JsonFormat(pattern = DEFAULT_PATTERN)
    private LocalDateTime startTime;
    @JsonFormat(pattern = DEFAULT_PATTERN)
    private LocalDateTime endTime;
    private Integer importStatus;
    private String errorDetail;
    @JsonFormat(pattern = DEFAULT_PATTERN)
    private LocalDateTime createDateTime;
}
