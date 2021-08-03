package com.sms.courier.entity.job;

import com.sms.courier.entity.job.common.JobApiTestCase;
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
