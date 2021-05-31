package com.sms.satp.mapper;

import com.sms.satp.dto.request.DataCollectionRequest;
import com.sms.satp.dto.request.TestDataRequest;
import com.sms.satp.entity.apitestcase.ApiTestCase;
import com.sms.satp.entity.datacollection.TestData;
import com.sms.satp.entity.env.GlobalEnvironment;
import com.sms.satp.entity.env.ProjectEnvironment;
import com.sms.satp.entity.job.common.JobApiTestCase;
import com.sms.satp.entity.job.common.JobDataCollection;
import com.sms.satp.entity.job.common.JobEnvironment;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = ParamInfoMapper.class)
public interface JobMapper {

    JobEnvironment toJobEnvironment(ProjectEnvironment projectEnvironment);

    JobEnvironment toJobEnvironment(GlobalEnvironment globalEnvironment);

    JobApiTestCase toJobApiTestCase(ApiTestCase apiTestCase);

    JobDataCollection toJobDataCollection(DataCollectionRequest dataCollectionRequest);

    TestData toTestDataEntity(TestDataRequest testDataRequest);
}
