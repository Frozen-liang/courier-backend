package com.sms.satp.entity.job;

import com.sms.satp.common.enums.JobStatus;
import com.sms.satp.entity.BaseEntity;
import com.sms.satp.entity.apitestcase.ApiTestCase;
import com.sms.satp.entity.datacollection.DataCollection;
import com.sms.satp.entity.env.ProjectEnvironment;
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

    private ApiTestCase apiTestCase;

    private ProjectEnvironment environment;

    private DataCollection dataCollection;

    private JobStatus jobStatus;
}
