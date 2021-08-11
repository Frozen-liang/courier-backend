package com.sms.courier.mapper;

import com.sms.courier.dto.request.AddSceneCaseApiRequest;
import com.sms.courier.dto.request.UpdateSceneCaseApiRequest;
import com.sms.courier.dto.response.SceneCaseApiConnResponse;
import com.sms.courier.dto.response.SceneCaseApiResponse;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import com.sms.courier.utils.EnumCommonUtils;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {EnumCommonUtils.class, ApiTestCaseMapper.class, ResponseHeadersVerificationMapper.class,
        ResponseResultVerificationMapper.class,
        ParamInfoMapper.class})
public interface SceneCaseApiMapper {

    SceneCaseApiEntity toSceneCaseApiByUpdateRequest(UpdateSceneCaseApiRequest updateSceneCaseApiRequest);

    SceneCaseApiResponse toSceneCaseApiDto(SceneCaseApiEntity sceneCaseApi);

    List<SceneCaseApiEntity> toSceneCaseApiList(List<UpdateSceneCaseApiRequest> sceneCaseApiList);

    List<SceneCaseApiEntity> toSceneCaseApiListByAddRequest(List<AddSceneCaseApiRequest> addSceneCaseApiRequestList);

    @Mapping(target = "apiTestCase.id", expression = "java(new org.bson.types.ObjectId().toString())")
    SceneCaseApiEntity toSceneCaseApi(AddSceneCaseApiRequest addSceneCaseApiRequest);

    SceneCaseApiConnResponse toSceneCaseApiConnResponse(SceneCaseApiEntity sceneCaseApi);
}
