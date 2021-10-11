package com.sms.courier.entity.job.common;

import com.sms.courier.entity.job.JobCaseApi;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractCaseJobEntity extends JobEntity {

    private JobCaseApi apiTestCase;

}
