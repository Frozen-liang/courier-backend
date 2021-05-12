package com.sms.satp.mapper;

import com.sms.satp.dto.AddSceneCaseApiRequest;
import com.sms.satp.dto.SceneCaseApiResponse;
import com.sms.satp.dto.UpdateSceneCaseApiRequest;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SceneCaseApiMapper {

    SceneCaseApi toSceneCaseApi(SceneCaseApiResponse dto);

    SceneCaseApi toSceneCaseApiByUpdateRequest(UpdateSceneCaseApiRequest updateSceneCaseApiRequest);

    SceneCaseApiResponse toSceneCaseApiDto(SceneCaseApi sceneCaseApi);

    List<SceneCaseApi> toSceneCaseApiList(List<SceneCaseApiResponse> sceneCaseApiList);

    List<SceneCaseApi> toSceneCaseApiListByAddRequest(List<AddSceneCaseApiRequest> addSceneCaseApiRequestList);
}
