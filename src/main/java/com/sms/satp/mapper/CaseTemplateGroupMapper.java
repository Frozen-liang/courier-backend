package com.sms.satp.mapper;

import com.sms.satp.dto.request.AddCaseTemplateGroupRequest;
import com.sms.satp.dto.request.UpdateCaseTemplateGroupRequest;
import com.sms.satp.dto.response.CaseTemplateGroupResponse;
import com.sms.satp.entity.group.CaseTemplateGroupEntity;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CaseTemplateGroupMapper {

    CaseTemplateGroupEntity toCaseTemplateGroupByAdd(AddCaseTemplateGroupRequest request);

    CaseTemplateGroupEntity toCaseTemplateGroupByUpdate(UpdateCaseTemplateGroupRequest request);

    List<CaseTemplateGroupResponse> toResponseList(List<CaseTemplateGroupEntity> caseTemplateGroups);
}
