package com.sms.courier.mapper;

import com.sms.courier.dto.response.ProjectImportFlowResponse;
import com.sms.courier.entity.project.ProjectImportFlowEntity;
import com.sms.courier.utils.EnumCommonUtils;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, unmappedTargetPolicy =
    ReportingPolicy.IGNORE, uses = EnumCommonUtils.class)
public interface ProjectImportFlowMapper {

    ProjectImportFlowResponse toProjectImportFlowResponse(ProjectImportFlowEntity entity);

}
