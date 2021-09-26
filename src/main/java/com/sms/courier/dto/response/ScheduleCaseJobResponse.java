package com.sms.courier.dto.response;

import com.sms.courier.entity.job.JobCaseApi;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ScheduleCaseJobResponse extends JobResponse {

    private JobCaseApi apiTestCase;
    private String name;
}
