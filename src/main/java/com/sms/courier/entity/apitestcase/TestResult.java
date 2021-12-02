package com.sms.courier.entity.apitestcase;

import com.sms.courier.common.enums.ResultType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestResult {

    private String jobId;

    private ResultType isSuccess;

    // Test time.
    private LocalDateTime testTime;

    // Test username.
    private String testUsername;
}
