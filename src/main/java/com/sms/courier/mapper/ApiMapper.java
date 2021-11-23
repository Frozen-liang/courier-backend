package com.sms.courier.mapper;

import com.sms.courier.common.enums.ApiStatus;
import com.sms.courier.common.enums.DocumentFileType;
import com.sms.courier.common.enums.GroupImportType;
import com.sms.courier.common.enums.SaveMode;
import com.sms.courier.dto.request.ApiCaseRequest;
import com.sms.courier.dto.request.ApiImportRequest;
import com.sms.courier.dto.request.ApiRequest;
import com.sms.courier.dto.response.ApiAndCaseResponse.ApiTestCaseResponse;
import com.sms.courier.dto.response.ApiResponse;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.api.common.ApiHistoryDetail;
import com.sms.courier.entity.project.ImportSourceVo;
import com.sms.courier.entity.project.ProjectImportSourceEntity;
import com.sms.courier.utils.EnumCommonUtils;
import com.sms.courier.webhook.response.WebhookApiResponse;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import java.util.Objects;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {ParamInfoMapper.class, EnumCommonUtils.class},
    imports = {DocumentFileType.class, SaveMode.class, ApiStatus.class, GroupImportType.class, Objects.class})
@SuppressFBWarnings
public interface ApiMapper {

    ApiResponse toDto(ApiEntity apiEntity);

    List<ApiResponse> toDtoList(List<ApiEntity> apiEntityList);

    ApiEntity toEntity(ApiRequest apiRequestDto);

    @Mapping(target = "documentType",
        expression =
            "java(DocumentFileType.getType(Objects"
                + ".requireNonNull(Objects.requireNonNull(request).getDocumentFileType())).getDocumentType())")
    @Mapping(target = "saveMode", expression = "java(SaveMode.getType(request.getSaveMode()))")
    @Mapping(target = "apiPresetStatus", expression = "java(ApiStatus.getType(request.getApiPresetStatus()))")
    @Mapping(target = "apiChangeStatus", expression = "java(ApiStatus.getType(request.getApiChangeStatus()))")
    ImportSourceVo toImportSource(ApiImportRequest request, String source);

    @Mapping(target = "documentType",
        expression = "java(projectImportSourceEntity.getDocumentType().getDocumentType())")
    @Mapping(target = "source", source = "documentUrl")
    ImportSourceVo toImportSource(ProjectImportSourceEntity projectImportSourceEntity);

    @Mapping(target = "caseCount", ignore = true)
    @Mapping(target = "sceneCaseCount", ignore = true)
    @Mapping(target = "otherProjectSceneCaseCount", ignore = true)
    ApiEntity toEntityByHistory(ApiHistoryDetail record);

    WebhookApiResponse toWebhookResponse(ApiEntity apiEntity);

    List<ApiTestCaseResponse> toApiTestCaseResponse(List<ApiCaseRequest> requests);
}