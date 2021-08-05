package com.sms.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sms.courier.common.constant.TimePatternConstant;
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
    @JsonFormat(pattern = TimePatternConstant.DEFAULT_PATTERN)
    private LocalDateTime startTime;
    @JsonFormat(pattern = TimePatternConstant.DEFAULT_PATTERN)
    private LocalDateTime endTime;
    private Integer importStatus;
    private String errorDetail;
    @JsonFormat(pattern = TimePatternConstant.DEFAULT_PATTERN)
    private LocalDateTime createDateTime;
}
