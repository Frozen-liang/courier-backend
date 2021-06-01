package com.sms.satp.mapper;

import com.sms.satp.entity.job.JobSceneCaseApi;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {ApiTestCaseMapper.class})
public interface SceneCaseJobMapper {

    List<JobSceneCaseApi> toJobSceneCaseApiList(List<SceneCaseApi> sceneCaseApiList);

    @Mapping(target = "jobApiTestCase", source = "apiTestCase")
    @Mapping(target = "apiType",
        expression = "java(sceneCaseApi.getApiType().getCode())")
    JobSceneCaseApi toJobSceneCaseApi(SceneCaseApi sceneCaseApi);

    List<JobSceneCaseApi> toJobSceneCaseApiListByTemplate(List<CaseTemplateApi> caseTemplateApiList);

    @Mapping(target = "jobApiTestCase", source = "apiTestCase")
    @Mapping(target = "apiType",
        expression = "java(caseTemplateApiList.getApiType().getCode())")
    JobSceneCaseApi toJobSceneCaseApiByTemplate(CaseTemplateApi caseTemplateApiList);
}
