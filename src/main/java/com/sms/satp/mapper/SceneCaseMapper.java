package com.sms.satp.mapper;

import com.sms.satp.dto.AddSceneCaseDto;
import com.sms.satp.dto.SceneCaseDto;
import com.sms.satp.dto.UpdateSceneCaseDto;
import com.sms.satp.entity.scenetest.SceneCase;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SceneCaseMapper {

    SceneCase toAddSceneCase(AddSceneCaseDto sceneCaseDto);

    SceneCase toUpdateSceneCase(UpdateSceneCaseDto sceneCaseDto);

    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    SceneCaseDto toDto(SceneCase sceneCase);
}
