package com.sms.satp.mapper;

import com.sms.satp.dto.SceneCaseApiDto;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SceneCaseApiMapper {

    SceneCaseApi toSceneCaseApi(SceneCaseApiDto dto);

    SceneCaseApiDto toSceneCaseApiDto(SceneCaseApi sceneCaseApi);

    List<SceneCaseApi> toSceneCaseApiList(List<SceneCaseApiDto> sceneCaseApiList);
}
