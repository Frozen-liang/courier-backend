package com.sms.satp.mapper;

import com.sms.satp.dto.request.ProjectImportSourceRequest;
import com.sms.satp.dto.response.ProjectImportSourceResponse;
import com.sms.satp.entity.project.ProjectImportSourceEntity;
import java.util.Optional;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, unmappedTargetPolicy =
    ReportingPolicy.IGNORE)
public interface ProjectImportSourceMapper {

    @Mapping(target = "documentType", expression = "java(com.sms.satp.common.enums.DocumentType.getType(request"
        + ".getDocumentType()))")
    @Mapping(target = "saveMode", expression = "java(com.sms.satp.common.enums.SaveMode.getType(request"
        + ".getSaveMode()))")
    @Mapping(target = "apiPresetStatus", expression = "java(com.sms.satp.common.enums.ApiStatus.getType(request"
        + ".getApiPresetStatus()))")
    @Mapping(target = "apiChangeStatus", expression = "java(com.sms.satp.common.enums.ApiStatus.getType(request"
        + ".getApiChangeStatus()))")
    ProjectImportSourceEntity toProjectImportSourceEntity(ProjectImportSourceRequest request,
        Optional<ProjectImportSourceEntity> oldImportSource);


    @Mapping(target = "documentType", expression = "java(entity.getDocumentType().getCode())")
    @Mapping(target = "saveMode", expression = "java(entity.getSaveMode().getCode())")
    @Mapping(target = "apiPresetStatus", expression = "java(entity.getApiPresetStatus().getCode())")
    @Mapping(target = "apiChangeStatus", expression = "java(entity.getApiChangeStatus().getCode())")
    ProjectImportSourceResponse toProjectImportSourceResponse(ProjectImportSourceEntity entity);


    @AfterMapping
    default void after(@MappingTarget ProjectImportSourceEntity newSourceEntity,
        Optional<ProjectImportSourceEntity> oldImportSource) {
        oldImportSource.ifPresent(sourceEntity -> {
            newSourceEntity.setCreateUserId(sourceEntity.getCreateUserId());
            newSourceEntity.setCreateDateTime(sourceEntity.getCreateDateTime());
        });
    }

}
