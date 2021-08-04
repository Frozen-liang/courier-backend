package com.sms.courier.mapper;

import com.sms.courier.dto.request.AddSceneCaseRequest;
import com.sms.courier.dto.request.UpdateSceneCaseRequest;
import com.sms.courier.dto.response.CaseTemplateApiResponse;
import com.sms.courier.dto.response.SceneCaseResponse;
import com.sms.courier.entity.scenetest.CaseTemplateApiConn;
import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
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
