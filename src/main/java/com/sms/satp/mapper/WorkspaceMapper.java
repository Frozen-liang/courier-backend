package com.sms.satp.mapper;

import com.sms.satp.dto.request.WorkspaceRequest;
import com.sms.satp.dto.response.WorkspaceResponse;
import com.sms.satp.entity.workspace.WorkspaceEntity;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WorkspaceMapper {

    WorkspaceResponse toDto(WorkspaceEntity workspace);

    List<WorkspaceResponse> toDtoList(List<WorkspaceEntity> workspaceList);

    WorkspaceEntity toEntity(WorkspaceRequest workspaceRequest);
}