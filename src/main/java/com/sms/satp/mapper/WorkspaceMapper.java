package com.sms.satp.mapper;

import static com.sms.satp.common.constant.TimePatternConstant.DEFAULT_PATTERN;

import com.sms.satp.dto.request.WorkspaceRequest;
import com.sms.satp.dto.response.WorkspaceResponse;
import com.sms.satp.entity.workspace.Workspace;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WorkspaceMapper {

    WorkspaceResponse toDto(Workspace workspace);

    List<WorkspaceResponse> toDtoList(List<Workspace> workspaceList);

    Workspace toEntity(WorkspaceRequest workspaceRequest);
}