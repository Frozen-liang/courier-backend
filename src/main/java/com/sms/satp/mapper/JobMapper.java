package com.sms.satp.mapper;

import static com.sms.satp.common.constant.TimePatternConstant.DEFAULT_PATTERN;

import com.sms.satp.common.constant.TimePatternConstant;
import com.sms.satp.dto.request.ApiTestRequest;
import com.sms.satp.dto.request.DataCollectionRequest;
import com.sms.satp.dto.request.TestDataRequest;
import com.sms.satp.dto.response.ApiTestCaseJobPageResponse;
import com.sms.satp.dto.response.ApiTestCaseJobResponse;
import com.sms.satp.dto.response.ApiTestCaseResponse;
import com.sms.satp.dto.response.SceneCaseJobResponse;
import com.sms.satp.entity.datacollection.TestData;
import com.sms.satp.entity.env.ProjectEnvironmentEntity;
import com.sms.satp.entity.job.ApiTestCaseJobEntity;
import com.sms.satp.entity.job.JobSceneCaseApi;
import com.sms.satp.entity.job.SceneCaseJobEntity;
import com.sms.satp.entity.job.common.JobApiTestCase;
import com.sms.satp.entity.job.common.JobDataCollection;
import com.sms.satp.entity.job.common.JobEnvironment;
import com.sms.satp.entity.scenetest.CaseTemplateApiEntity;
import com.sms.satp.entity.scenetest.SceneCaseApiEntity;
import com.sms.satp.utils.EnumCommonUtils;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {EnumCommonUtils.class, ParamInfoMapper.class})
public interface JobMapper {

    JobEnvironment toJobEnvironment(ProjectEnvironmentEntity projectEnvironment);

    JobApiTestCase toJobApiTestCase(ApiTestCaseResponse apiTestCaseResponse);

    JobApiTestCase toJobApiTestCase(ApiTestRequest apiTestRequest);

    JobDataCollection toJobDataCollection(DataCollectionRequest dataCollectionRequest);

    TestData toTestDataEntity(TestDataRequest testDataRequest);

    ApiTestCaseJobResponse toApiTestCaseJobResponse(ApiTestCaseJobEntity apiTestCaseJob);

    @Mapping(target = "testDateTime", source = "createDateTime", dateFormat = DEFAULT_PATTERN)
    @Mapping(target = "testUser", source = "createUserName")
    @Mapping(target = "testReport", source = "apiTestCaseJob.apiTestCase.jobApiTestCase.caseReport")
    ApiTestCaseJobPageResponse toApiTestCaseJobPageResponse(ApiTestCaseJobEntity apiTestCaseJob);

    List<JobSceneCaseApi> toJobSceneCaseApiList(List<SceneCaseApiEntity> sceneCaseApiList);

    @Mapping(target = "jobApiTestCase", source = "apiTestCase")
    JobSceneCaseApi toJobSceneCaseApi(SceneCaseApiEntity sceneCaseApi);

    List<JobSceneCaseApi> toJobSceneCaseApiListByTemplate(List<CaseTemplateApiEntity> caseTemplateApiList);

    @Mapping(target = "jobApiTestCase", source = "apiTestCase")
    JobSceneCaseApi toJobSceneCaseApiByTemplate(CaseTemplateApiEntity caseTemplateApiList);

    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = TimePatternConstant.DEFAULT_PATTERN)
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = TimePatternConstant.DEFAULT_PATTERN)
    SceneCaseJobResponse toSceneCaseJobResponse(SceneCaseJobEntity sceneCaseJob);
}
