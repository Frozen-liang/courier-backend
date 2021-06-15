package com.sms.satp.entity.job;

import com.sms.satp.entity.job.common.JobApiTestCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobCaseApi {

    private JobApiTestCase jobApiTestCase;
}
