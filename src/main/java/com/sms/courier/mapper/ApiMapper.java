package com.sms.courier.mapper;

import com.sms.courier.common.enums.ApiStatus;
import com.sms.courier.common.enums.DocumentFileType;
import com.sms.courier.common.enums.GroupImportType;
import com.sms.courier.common.enums.SaveMode;
import com.sms.courier.dto.request.ApiImportRequest;
import com.sms.courier.dto.request.ApiRequest;
import com.sms.courier.dto.response.ApiResponse;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.project.ImportSourceVo;
import com.sms.courier.entity.project.ProjectImportSourceEntity;
import com.sms.courier.utils.EnumCommonUtils;
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
    @Mapping(target = "apiPresetStatus", expression = "java(ApiStatus.getType(request.getSaveMode()))")
    @Mapping(target = "apiChangeStatus", expression = "java(ApiStatus.getType(request.getSaveMode()))")
    @Mapping(target = "groupImportType", expression = "java(GroupImportType.getType(request.getSaveMode()))")
    ImportSourceVo toImportSource(ApiImportRequest request, String source);

    @Mapping(target = "documentType",
        expression = "java(projectImportSourceEntity.getDocumentType().getDocumentType())")
    @Mapping(target = "source", source = "documentUrl")
    ImportSourceVo toImportSource(ProjectImportSourceEntity projectImportSourceEntity);

}