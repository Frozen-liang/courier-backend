package com.sms.courier.mapper;

import com.sms.courier.dto.request.AddSceneCaseRequest;
import com.sms.courier.dto.request.UpdateSceneCaseRequest;
import com.sms.courier.dto.response.CaseTemplateApiResponse;
import com.sms.courier.dto.response.SceneCaseConnResponse;
import com.sms.courier.dto.response.SceneCaseResponse;
import com.sms.courier.entity.scenetest.CaseTemplateApiConn;
import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
import com.sms.courier.utils.EnumCommonUtils;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = EnumCommonUtils.class)
public interface SceneCaseMapper {

    @Mapping(target = "reviewStatus", expression = "java(com.sms.courier.common.enums.ReviewStatus"
        + ".getType(sceneCaseDto.getReviewStatus()))")
    SceneCaseEntity toAddSceneCase(AddSceneCaseRequest sceneCaseDto);

    @Mapping(target = "reviewStatus", expression = "java(com.sms.courier.common.enums.ReviewStatus"
        + ".getType(sceneCaseDto.getReviewStatus()))")
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

    @Mapping(target = "envDataCollConnList", ignore = true)
    SceneCaseConnResponse toConnResponse(SceneCaseResponse sceneCaseResponse);

}
