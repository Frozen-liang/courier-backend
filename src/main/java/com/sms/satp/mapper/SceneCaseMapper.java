package com.sms.satp.mapper;

import com.sms.satp.dto.request.AddSceneCaseRequest;
import com.sms.satp.dto.request.UpdateSceneCaseRequest;
import com.sms.satp.dto.response.SceneCaseResponse;
import com.sms.satp.entity.scenetest.SceneCase;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SceneCaseMapper {

    SceneCase toAddSceneCase(AddSceneCaseRequest sceneCaseDto);

    SceneCase toUpdateSceneCase(UpdateSceneCaseRequest sceneCaseDto);

    SceneCaseResponse toDto(SceneCase sceneCase);
}
