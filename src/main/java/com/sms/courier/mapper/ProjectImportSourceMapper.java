package com.sms.courier.mapper;

import com.sms.courier.dto.request.ProjectImportSourceRequest;
import com.sms.courier.dto.response.ProjectImportSourceResponse;
import com.sms.courier.entity.project.ProjectImportSourceEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, unmappedTargetPolicy =
    ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface ProjectImportSourceMapper {

    @Mapping(target = "documentType", expression = "java(com.sms.courier.common.enums.DocumentUrlType.getType(request"
        + ".getDocumentType()))")
    @Mapping(target = "saveMode", expression = "java(com.sms.courier.common.enums.SaveMode.getType(request"
        + ".getSaveMode()))")
    @Mapping(target = "apiPresetStatus", expression = "java(com.sms.courier.common.enums.ApiStatus.getType(request"
        + ".getApiPresetStatus()))")
    @Mapping(target = "apiChangeStatus", expression = "java(com.sms.courier.common.enums.ApiStatus.getType(request"
        + ".getApiChangeStatus()))")
    ProjectImportSourceEntity toProjectImportSourceEntity(ProjectImportSourceRequest request);


    @Mapping(target = "documentType", expression = "java(entity.getDocumentType().getCode())")
    @Mapping(target = "saveMode", expression = "java(entity.getSaveMode().getCode())")
    @Mapping(target = "apiPresetStatus", expression = "java(entity.getApiPresetStatus().getCode())")
    @Mapping(target = "apiChangeStatus", expression = "java(entity.getApiChangeStatus().getCode())")
    ProjectImportSourceResponse toProjectImportSourceResponse(ProjectImportSourceEntity entity);


}
