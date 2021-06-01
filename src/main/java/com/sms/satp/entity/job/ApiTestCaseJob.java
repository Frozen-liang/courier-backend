package com.sms.satp.entity.job;

import com.sms.satp.entity.BaseEntity;
import com.sms.satp.entity.job.common.JobApiTestCase;
import com.sms.satp.entity.job.common.JobDataCollection;
import com.sms.satp.entity.job.common.JobEnvironment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "ApiTestCaseJob")
public class ApiTestCaseJob extends BaseEntity {

    private JobApiTestCase apiTestCase;

    private JobEnvironment environment;

    private JobDataCollection dataCollection;

    private Integer jobStatus;

    /**
     * 测试人员.
     */
    private String createUserName;
}
