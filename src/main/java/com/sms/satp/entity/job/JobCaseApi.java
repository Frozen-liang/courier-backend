package com.sms.satp.entity.job;

import com.sms.satp.entity.job.common.JobApiTestCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobCaseApi {

    private JobApiTestCase jobApiTestCase;
}
