package com.sms.satp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.satp.common.enums.ImportStatus;
import com.sms.satp.dto.request.ProjectImportSourceRequest;
import com.sms.satp.dto.response.ProjectImportFlowResponse;
import com.sms.satp.dto.response.ProjectImportSourceResponse;
import com.sms.satp.entity.project.ProjectImportFlowEntity;
import com.sms.satp.entity.project.ProjectImportSourceEntity;
import com.sms.satp.mapper.ProjectImportSourceMapper;
import com.sms.satp.repository.CommonDeleteRepository;
import com.sms.satp.repository.ProjectImportFlowRepository;
import com.sms.satp.repository.ProjectImportSourceRepository;
import com.sms.satp.service.impl.ProjectImportSourceServiceImpl;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author zixi.gao
 * @create 2021-07-15 15:33
 */

@DisplayName("Tests for ProjectImportSourceService")
public class ProjectImportSourceServiceTest {

    private final ProjectImportSourceRepository projectImportSourceRepository = mock(
        ProjectImportSourceRepository.class);
    private final ProjectImportSourceMapper projectImportSourceMapper = mock(ProjectImportSourceMapper.class);
    private final ProjectImportFlowRepository projectImportFlowRepository = mock(ProjectImportFlowRepository.class);
    private final CommonDeleteRepository commonDeleteRepository = mock(CommonDeleteRepository.class);
    private final ProjectImportSourceService projectImportSourceService = new ProjectImportSourceServiceImpl(
        projectImportSourceMapper, projectImportSourceRepository, projectImportFlowRepository, commonDeleteRepository);
    private final ProjectImportSourceEntity projectImportSourceEntity = ProjectImportSourceEntity.builder().id(ID)
        .build();
    private final ProjectImportSourceResponse projectImportSourceResponse = ProjectImportSourceResponse
        .builder().id(ID).build();
    private final ProjectImportSourceRequest projectImportSourceRequest = ProjectImportSourceRequest.builder().id(ID)
        .build();
    private final ProjectImportFlowResponse projectImportFlowResponse = ProjectImportFlowResponse.builder().id(ID)
        .build();
    private static final String ID = ObjectId.get().toString();
    private static final List<String> ID_LIST = Collections.singletonList(ID);

    @Test
    @DisplayName("Test the create method in the ProjectImportSource Service")
    public void create_test() {
        when(projectImportSourceRepository
            .insert(projectImportSourceMapper.toProjectImportSourceEntity(projectImportSourceRequest)))
            .thenReturn(projectImportSourceEntity);
        assertThat(projectImportSourceService.create(projectImportSourceRequest)).isTrue();
    }

    @Test
    @DisplayName("Test the update method in the ProjectImportSource Service")
    public void update_test() {
        when(projectImportSourceRepository
            .save(projectImportSourceMapper.toProjectImportSourceEntity(projectImportSourceRequest)))
            .thenReturn(projectImportSourceEntity);
        assertThat(projectImportSourceService.update(projectImportSourceRequest)).isTrue();
    }

    @Test
    @DisplayName("Test the findById method in the ProjectImportSource Service")
    public void findById_test() {
        when(projectImportSourceRepository.findById(ID)).thenReturn(Optional.of(projectImportSourceEntity));
        when(projectImportSourceMapper.toProjectImportSourceResponse(projectImportSourceEntity))
            .thenReturn(projectImportSourceResponse);
        ProjectImportSourceResponse result = projectImportSourceService.findById(ID);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("Test the findByProjectId method in the ProjectImportSource Service")
    public void findByProjectId_test() {
        when(projectImportSourceRepository.findByProjectIdAndRemovedIsFalse(ID))
            .thenReturn(List.of(ProjectImportSourceResponse.builder().build()));
        when(projectImportFlowRepository.findFirstByImportSourceIdOrderByCreateDateTimeDesc(any()))
            .thenReturn(ProjectImportFlowEntity.builder().importStatus(ImportStatus.SUCCESS).build());
        List<ProjectImportSourceResponse> result = projectImportSourceService.findByProjectId(ID);
        assertThat(result).allMatch(
            (projectImportSourceResponse) -> ImportStatus.SUCCESS.getCode() == projectImportSourceResponse
                .getImportStatus());
    }

    @Test
    @DisplayName("Test the delete method in the ProjectImportSource Service")
    public void delete_test() {
        when(commonDeleteRepository.deleteByIds(ID_LIST, ProjectImportSourceEntity.class)).thenReturn(true);
        assertThat(projectImportSourceService.delete(ID_LIST)).isTrue();
    }

    @Test
    @DisplayName("Test the findByIds method in the ProjectImportSource Service")
    public void findByIds_test() {
        when(projectImportSourceRepository.findAllById(ID_LIST))
            .thenReturn(List.of(ProjectImportSourceEntity.builder().build()));
        assertThat(projectImportSourceService.findByIds(ID_LIST)).isNotNull();
    }

    @Test
    @DisplayName("Test the getProjectImportFlow method in the ProjectImportSource Service")
    public void getProjectImportFlow_test() {
        when(projectImportFlowRepository.findFirstByProjectId(ID)).thenReturn(projectImportFlowResponse);
        ProjectImportFlowResponse result = projectImportSourceService.getProjectImportFlow(ID);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(ID);
    }

}
