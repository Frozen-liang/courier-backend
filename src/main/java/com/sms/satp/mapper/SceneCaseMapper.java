package com.sms.satp.mapper;

import com.sms.satp.dto.request.AddSceneCaseRequest;
import com.sms.satp.dto.request.UpdateSceneCaseRequest;
import com.sms.satp.dto.response.CaseTemplateApiResponse;
import com.sms.satp.dto.response.SceneCaseResponse;
import com.sms.satp.entity.scenetest.CaseTemplateApiConn;
import com.sms.satp.entity.scenetest.CaseTemplateApiEntity;
import com.sms.satp.entity.scenetest.SceneCaseEntity;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SceneCaseMapper {

    SceneCaseEntity toAddSceneCase(AddSceneCaseRequest sceneCaseDto);

    SceneCaseEntity toUpdateSceneCase(UpdateSceneCaseRequest sceneCaseDto);

    SceneCaseResponse toDto(SceneCaseEntity sceneCase);

    List<CaseTemplateApiConn> toCaseTemplateApiConnList(List<CaseTemplateApiEntity> caseTemplateApiList);

    @Mapping(target = "caseTemplateApiId", source = "id")
    @Mapping(target = "execute", source = "apiTestCase.execute")
    CaseTemplateApiConn toCaseTemplateApiConn(CaseTemplateApiEntity caseTemplateApi);

    List<CaseTemplateApiConn> toCaseTemplateApiConnListByResponse(
        List<CaseTemplateApiResponse> caseTemplateApiResponseList);

    @Mapping(target = "caseTemplateApiId", source = "id")
    @Mapping(target = "execute", source = "apiTestCase.execute")
    CaseTemplateApiConn toCaseTemplateApiConnByResponse(CaseTemplateApiResponse caseTemplateApi);
}
