package com.sms.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.courier.common.constant.TimePatternConstant;
import com.sms.courier.entity.schedule.JobRecord;
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
public class ScheduleRecordResponse {

    private String id;

    private String scheduleId;

    private String scheduleName;

    private String projectId;

    private String workspaceId;

    @JsonProperty("isExecute")
    private boolean execute;

    private String message;

    private String code;

    private int success;

    private int fail;

    private List<JobRecord> jobRecords;

    @JsonFormat(pattern = TimePatternConstant.DEFAULT_PATTERN)
    private LocalDateTime createDateTime;

    @JsonFormat(pattern = TimePatternConstant.DEFAULT_PATTERN)
    private LocalDateTime testCompletionTime;
}
