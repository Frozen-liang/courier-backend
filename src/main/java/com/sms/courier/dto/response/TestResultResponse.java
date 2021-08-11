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
public class TestResultResponse {

    private String jobId;

    private Integer status;

    private Integer isSuccess;

    private String errCode;

    private String failMessage;

    // Test time.
    @JsonFormat(pattern = TimePatternConstant.DEFAULT_PATTERN)
    private LocalDateTime testTime;

    // Test username.
    private String testUsername;
}
