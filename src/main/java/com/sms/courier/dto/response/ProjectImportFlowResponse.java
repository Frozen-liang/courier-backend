package com.sms.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sms.courier.common.constant.TimePatternConstant;
import com.sms.courier.entity.project.ApiImportRollbackRecord;
import com.sms.courier.entity.project.ApiRecord;
import java.time.LocalDateTime;
import java.util.List;
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
    private String createUsername;
    @JsonFormat(pattern = TimePatternConstant.DEFAULT_PATTERN)
    private LocalDateTime startTime;
    @JsonFormat(pattern = TimePatternConstant.DEFAULT_PATTERN)
    private LocalDateTime endTime;
    private Integer importStatus;
    private String errorDetail;
    @JsonFormat(pattern = TimePatternConstant.DEFAULT_PATTERN)
    private LocalDateTime createDateTime;
    private List<ApiRecord> addedApi;
    private List<ApiRecord> deletedApi;
    private List<ApiRecord> updatedApi;
    private ApiImportRollbackRecord rollbackRecord;
}
