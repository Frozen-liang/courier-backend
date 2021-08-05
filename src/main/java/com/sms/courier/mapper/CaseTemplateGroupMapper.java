package com.sms.courier.mapper;

import com.sms.courier.dto.request.CaseTemplateGroupRequest;
import com.sms.courier.dto.response.CaseTemplateGroupResponse;
import com.sms.courier.entity.group.CaseTemplateGroupEntity;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CaseTemplateGroupMapper {

    CaseTemplateGroupEntity toCaseTemplateGroupEntity(CaseTemplateGroupRequest request);

    List<CaseTemplateGroupResponse> toResponse(List<CaseTemplateGroupEntity> caseTemplateGroupEntityList);
}
