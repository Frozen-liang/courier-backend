package com.sms.satp.mapper;

import com.sms.satp.dto.response.ProjectImportFlowResponse;
import com.sms.satp.entity.project.ProjectImportFlowEntity;
import com.sms.satp.utils.EnumCommonUtils;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, unmappedTargetPolicy =
    ReportingPolicy.IGNORE, uses = EnumCommonUtils.class)
public interface ProjectImportFlowMapper {

    ProjectImportFlowResponse toProjectImportFlowResponse(ProjectImportFlowEntity entity);

}
