package com.sms.satp.mapper;

import com.sms.satp.dto.request.CaseTemplateGroupRequest;
import com.sms.satp.dto.response.CaseTemplateGroupResponse;
import com.sms.satp.dto.response.TreeResponse;
import com.sms.satp.entity.group.CaseTemplateGroupEntity;
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
