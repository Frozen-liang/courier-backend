package com.sms.satp.mapper;

import com.sms.satp.common.enums.OperationType;
import com.sms.satp.dto.SceneCaseApiLogDto;
import com.sms.satp.entity.scenetest.CaseTemplate;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import com.sms.satp.entity.scenetest.SceneCase;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import com.sms.satp.entity.scenetest.SceneCaseApiLog;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SceneCaseApiLogMapper {

    SceneCaseApiLogDto toSceneCaseApiLogDto(SceneCaseApiLog sceneCaseApiLog);

    SceneCaseApiLog toSceneCaseApiLog(SceneCaseApiLogDto dto);

    @Mapping(target = "sceneCaseApi.createDateTime", ignore = true)
    @Mapping(target = "sceneCaseApi.createUserId", ignore = true)
    @Mapping(source = "sceneCaseApi.apiName", target = "operationTarget")
    @Mapping(source = "sceneCaseApi.id", target = "sceneCaseApiId")
    SceneCaseApiLogDto toDtoBySceneCaseApi(SceneCaseApi sceneCaseApi, OperationType operationType);

    @Mapping(target = "sceneCaseApi.createDateTime", ignore = true)
    @Mapping(target = "sceneCaseApi.createUserId", ignore = true)
    @Mapping(source = "sceneCase.name", target = "operationTarget")
    @Mapping(source = "sceneCase.id", target = "sceneCaseId")
    SceneCaseApiLogDto toDtoBySceneCase(SceneCase sceneCase, OperationType operationType);

    @Mapping(target = "caseTemplate.createDateTime", ignore = true)
    @Mapping(target = "caseTemplate.createUserId", ignore = true)
    @Mapping(source = "caseTemplate.name", target = "operationTarget")
    @Mapping(source = "caseTemplate.id", target = "caseTemplateId")
    SceneCaseApiLogDto toDtoBySceneCaseTemplate(CaseTemplate caseTemplate, OperationType operationType);

    @Mapping(target = "caseTemplateApi.createDateTime", ignore = true)
    @Mapping(target = "caseTemplateApi.createUserId", ignore = true)
    @Mapping(source = "caseTemplateApi.apiName", target = "operationTarget")
    @Mapping(source = "caseTemplateApi.id", target = "caseTemplateApiId")
    SceneCaseApiLogDto toDtoBySceneCaseTemplateApi(CaseTemplateApi caseTemplateApi, OperationType operationType);

}
