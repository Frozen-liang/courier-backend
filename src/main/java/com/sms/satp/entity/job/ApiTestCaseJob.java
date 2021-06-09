package com.sms.satp.entity.job;

import com.sms.satp.common.enums.JobStatus;
import com.sms.satp.entity.BaseEntity;
import com.sms.satp.entity.job.common.JobDataCollection;
import com.sms.satp.entity.job.common.JobEnvironment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "ApiTestCaseJob")
public class ApiTestCaseJob extends BaseEntity {

    private JobCaseApi apiTestCase;

    private JobEnvironment environment;

    private JobDataCollection dataCollection;

    private JobStatus jobStatus;

    private String message;
    /**
     * 测试人员.
     */
    private String createUserName;
}
