package com.sms.satp.mapper;

import com.sms.satp.dto.request.AddSceneCaseRequest;
import com.sms.satp.dto.request.UpdateSceneCaseRequest;
import com.sms.satp.dto.response.CaseTemplateApiResponse;
import com.sms.satp.dto.response.SceneCaseResponse;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import com.sms.satp.entity.scenetest.CaseTemplateApiConn;
import com.sms.satp.entity.scenetest.SceneCase;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SceneCaseMapper {

    SceneCase toAddSceneCase(AddSceneCaseRequest sceneCaseDto);

    SceneCase toUpdateSceneCase(UpdateSceneCaseRequest sceneCaseDto);

    SceneCaseResponse toDto(SceneCase sceneCase);

    List<CaseTemplateApiConn> toCaseTemplateApiConnList(List<CaseTemplateApi> caseTemplateApiList);

    @Mapping(target = "caseTemplateApiId", source = "id")
    @Mapping(target = "execute", source = "apiTestCase.execute")
    CaseTemplateApiConn toCaseTemplateApiConn(CaseTemplateApi caseTemplateApi);

    List<CaseTemplateApiConn> toCaseTemplateApiConnListByResponse(
        List<CaseTemplateApiResponse> caseTemplateApiResponseList);

    @Mapping(target = "caseTemplateApiId", source = "id")
    @Mapping(target = "execute", source = "apiTestCase.execute")
    CaseTemplateApiConn toCaseTemplateApiConnByResponse(CaseTemplateApiResponse caseTemplateApi);
}
