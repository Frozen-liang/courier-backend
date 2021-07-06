package com.sms.satp.mapper;

import com.sms.satp.common.enums.ApiStatus;
import com.sms.satp.common.enums.DocumentFileType;
import com.sms.satp.common.enums.GroupImportType;
import com.sms.satp.common.enums.SaveMode;
import com.sms.satp.dto.request.ApiImportRequest;
import com.sms.satp.dto.request.ApiRequest;
import com.sms.satp.dto.response.ApiResponse;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.project.ImportSourceVo;
import com.sms.satp.entity.project.ProjectImportSourceEntity;
import com.sms.satp.parser.common.DocumentDefinition;
import com.sms.satp.utils.EnumCommonUtils;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {ParamInfoMapper.class, EnumCommonUtils.class},
    imports = {DocumentFileType.class, SaveMode.class, ApiStatus.class, GroupImportType.class})
public interface ApiMapper {

    ApiResponse toDto(ApiEntity apiEntity);

    List<ApiResponse> toDtoList(List<ApiEntity> apiEntityList);

    ApiEntity toEntity(ApiRequest apiRequestDto);

    @Mapping(target = "documentType", expression = "java(DocumentFileType.getType(request.getDocumentFileType()).getDocumentType())")
    @Mapping(target = "saveMode", expression = "java(SaveMode.getType(request.getSaveMode()))")
    @Mapping(target = "apiPresetStatus", expression = "java(ApiStatus.getType(request.getSaveMode()))")
    @Mapping(target = "apiChangeStatus", expression = "java(ApiStatus.getType(request.getSaveMode()))")
    @Mapping(target = "groupImportType", expression = "java(GroupImportType.getType(request.getSaveMode()))")
    ImportSourceVo toImportSource(ApiImportRequest request, String source);

    @Mapping(target = "documentType", expression = "java(projectImportSourceEntity.getDocumentType().getDocumentType())")
    @Mapping(target = "source", source = "documentUrl")
    ImportSourceVo toImportSource(ProjectImportSourceEntity projectImportSourceEntity);

}