package com.sms.courier.mapper;

import com.sms.courier.dto.request.WorkspaceRequest;
import com.sms.courier.dto.response.WorkspaceResponse;
import com.sms.courier.entity.workspace.WorkspaceEntity;
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