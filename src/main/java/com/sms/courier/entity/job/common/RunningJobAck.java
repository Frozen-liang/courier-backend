package com.sms.courier.entity.job.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RunningJobAck {

    private String jobId;
    // 当前引擎订阅的地址
    private String destination;
}
