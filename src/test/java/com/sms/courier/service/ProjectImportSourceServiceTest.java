package com.sms.courier.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.common.enums.ImportStatus;
import com.sms.courier.dto.request.ProjectImportFlowPageRequest;
import com.sms.courier.dto.request.ProjectImportSourceRequest;
import com.sms.courier.dto.response.ProjectImportFlowResponse;
import com.sms.courier.dto.response.ProjectImportSourceResponse;
import com.sms.courier.entity.mongo.QueryVo;
import com.sms.courier.entity.project.ProjectImportFlowEntity;
import com.sms.courier.entity.project.ProjectImportSourceEntity;
import com.sms.courier.mapper.ProjectImportSourceMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.ProjectImportFlowRepository;
import com.sms.courier.repository.ProjectImportSourceRepository;
import com.sms.courier.service.impl.ProjectImportSourceServiceImpl;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

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
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final ProjectImportSourceService projectImportSourceService = new ProjectImportSourceServiceImpl(
        projectImportSourceMapper, projectImportSourceRepository, projectImportFlowRepository, commonRepository);
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
        when(projectImportSourceRepository.findFirstByProjectIdAndRemovedIsFalseOrderByCreateDateTimeDesc(ID))
            .thenReturn(ProjectImportSourceResponse.builder().build());
        when(projectImportFlowRepository.findFirstByImportSourceIdOrderByCreateDateTimeDesc(any()))
            .thenReturn(ProjectImportFlowEntity.builder().importStatus(ImportStatus.SUCCESS).build());
        ProjectImportSourceResponse result = projectImportSourceService.findByProjectId(ID);
        assertThat(result.getImportStatus()).isEqualTo(ImportStatus.SUCCESS.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the ProjectImportSource Service")
    public void delete_test() {
        when(commonRepository.deleteByIds(ID_LIST, ProjectImportSourceEntity.class)).thenReturn(true);
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

    @Test
    @DisplayName("Test the pageProjectImportFlow method in the ProjectImportSource Service")
    public void pageProjectImportFlow_test() {
        ProjectImportFlowPageRequest request = ProjectImportFlowPageRequest.builder().build();
        when(commonRepository.page(any(QueryVo.class),any(),any())).thenReturn(Page.empty());
        Page<ProjectImportFlowResponse> page = projectImportSourceService
            .pageProjectImportFlow(request);
        assertThat(page).isEmpty();
    }
}
