package com.sms.courier.initialize.impl;

import com.sms.courier.entity.project.ProjectEntity;
import com.sms.courier.entity.workspace.WorkspaceEntity;
import com.sms.courier.initialize.ApiCaseCount;
import com.sms.courier.initialize.ApiCountProperties;
import com.sms.courier.repository.ApiRepository;
import com.sms.courier.repository.CustomizedApiRepository;
import com.sms.courier.repository.CustomizedApiTestCaseRepository;
import com.sms.courier.repository.ProjectRepository;
import com.sms.courier.repository.WorkspaceRepository;
import java.util.List;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Tests for ApiCaseCountInitializerTest")
public class ApiCaseCountInitializerTest {

    private final ApplicationContext applicationContext = mock(ApplicationContext.class);
    private final WorkspaceRepository workspaceRepository = mock(WorkspaceRepository.class);
    private final ProjectRepository projectRepository = mock(ProjectRepository.class);
    private final CustomizedApiTestCaseRepository customizedApiTestCaseRepository =
        mock(CustomizedApiTestCaseRepository.class);
    private final CustomizedApiRepository customizedApiRepository = mock(CustomizedApiRepository.class);
    private final ApiCountProperties apiCountProperties = mock(ApiCountProperties.class);
    private final ApiCaseCountInitializer apiCaseCountInitializer = new ApiCaseCountInitializer(apiCountProperties);
    private final String ID = new ObjectId().toString();

    @Test
    @DisplayName("Test the init method in the ApiCaseCountInitializer")
    public void init_not_exist_test() {
        when(apiCountProperties.getCaseCount()).thenReturn(Boolean.FALSE);
        apiCaseCountInitializer.init(applicationContext);
        verify(workspaceRepository, times(0)).findAll();
    }

    @Test
    @DisplayName("Test the init method in the ApiCaseCountInitializer")
    public void init_exist_test() {
        when(apiCountProperties.getCaseCount()).thenReturn(Boolean.TRUE);
        when(applicationContext.getBean(WorkspaceRepository.class)).thenReturn(workspaceRepository);
        when(applicationContext.getBean(ProjectRepository.class)).thenReturn(projectRepository);
        when(applicationContext.getBean(CustomizedApiTestCaseRepository.class))
            .thenReturn(customizedApiTestCaseRepository);
        when(applicationContext.getBean(CustomizedApiRepository.class)).thenReturn(customizedApiRepository);
        List<WorkspaceEntity> workspaceEntityList = Lists.newArrayList(WorkspaceEntity.builder().id(ID).build());
        when(workspaceRepository.findAll()).thenReturn(workspaceEntityList);
        List<ProjectEntity> projectEntityList = Lists.newArrayList(ProjectEntity.builder().id(ID).build());
        when(projectRepository.findAllByWorkspaceId(any())).thenReturn(projectEntityList);
        List<ApiCaseCount> caseCountList = Lists.newArrayList(ApiCaseCount.builder().apiId(ID).build());
        when(customizedApiTestCaseRepository.findProjectIdAndGroupByApiId(any(), anyBoolean()))
            .thenReturn(caseCountList);
        apiCaseCountInitializer.init(applicationContext);
        verify(customizedApiRepository, times(1)).updateCountFieldByIds(any(), any());
    }

    @Test
    @DisplayName("An exception occurred while ApiCaseCountInitializer init")
    public void init_exist_test_thenException() {
        when(apiCountProperties.getCaseCount()).thenReturn(Boolean.TRUE);
        when(applicationContext.getBean(WorkspaceRepository.class)).thenThrow(new RuntimeException());
        apiCaseCountInitializer.init(applicationContext);
        verify(customizedApiRepository, times(0)).updateCountFieldByIds(any(), any());
    }
}
