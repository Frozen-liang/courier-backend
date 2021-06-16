package com.sms.satp.mapper;

import com.sms.satp.dto.request.AddSceneCaseApiRequest;
import com.sms.satp.dto.request.UpdateSceneCaseApiRequest;
import com.sms.satp.dto.response.SceneCaseApiResponse;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = ApiTestCaseMapper.class)
public interface SceneCaseApiMapper {

    @Mapping(target = "apiTestCase", source = "apiTestCaseRequest")
    SceneCaseApi toSceneCaseApiByUpdateRequest(UpdateSceneCaseApiRequest updateSceneCaseApiRequest);

    @Mapping(target = "apiTestCaseResponse", source = "apiTestCase")
    @Mapping(target = "apiType",
        expression = "java(sceneCaseApi.getApiType().getCode())")
    SceneCaseApiResponse toSceneCaseApiDto(SceneCaseApi sceneCaseApi);

    List<SceneCaseApi> toSceneCaseApiList(List<UpdateSceneCaseApiRequest> sceneCaseApiList);

    List<SceneCaseApi> toSceneCaseApiListByAddRequest(List<AddSceneCaseApiRequest> addSceneCaseApiRequestList);

    @Mapping(target = "apiTestCase.id", expression = "java(new org.bson.types.ObjectId().toString())")
    @Mapping(target = "apiTestCase", source = "apiTestCaseRequest")
    SceneCaseApi toSceneCaseApi(AddSceneCaseApiRequest addSceneCaseApiRequest);

}
