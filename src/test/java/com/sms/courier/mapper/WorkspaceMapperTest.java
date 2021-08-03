package com.sms.courier.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.courier.dto.request.WorkspaceRequest;
import com.sms.courier.dto.response.WorkspaceResponse;
import com.sms.courier.entity.workspace.WorkspaceEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for WorkspaceMapper")
class WorkspaceMapperTest {

    private WorkspaceMapper workspaceMapper = new WorkspaceMapperImpl();

    private static final Integer SIZE = 10;
    private static final String NAME = "workspace";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the Workspace's entity object to a dto object")
    void entity_to_dto() {
        WorkspaceEntity workspace = WorkspaceEntity.builder()
            .name(NAME)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        WorkspaceResponse workspaceResponse = workspaceMapper.toDto(workspace);
        assertThat(workspaceResponse.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Test the method for converting an Workspace entity list object to a dto list object")
    void workspaceList_to_workspaceDtoList() {
        List<WorkspaceEntity> workspaces = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            workspaces.add(WorkspaceEntity.builder().name(NAME).build());
        }
        List<WorkspaceResponse> workspaceResponseList = workspaceMapper.toDtoList(workspaces);
        assertThat(workspaceResponseList).hasSize(SIZE);
        assertThat(workspaceResponseList).allMatch(result -> StringUtils.equals(result.getName(), NAME));
    }

    @Test
    @DisplayName("Test the method to convert the Workspace's dto object to a entity object")
    void dto_to_entity() {
        WorkspaceRequest workspaceRequest = WorkspaceRequest.builder()
            .name(NAME)
            .build();
        WorkspaceEntity workspace = workspaceMapper.toEntity(workspaceRequest);
        assertThat(workspace.getName()).isEqualTo(NAME);
    }

}