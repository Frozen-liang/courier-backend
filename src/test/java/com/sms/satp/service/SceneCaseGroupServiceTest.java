package com.sms.satp.service;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.AddSceneCaseGroupRequest;
import com.sms.satp.dto.request.SearchSceneCaseGroupRequest;
import com.sms.satp.dto.request.UpdateSceneCaseGroupRequest;
import com.sms.satp.dto.response.SceneCaseGroupResponse;
import com.sms.satp.dto.response.SceneCaseResponse;
import com.sms.satp.entity.group.SceneCaseGroup;
import com.sms.satp.entity.scenetest.SceneCase;
import com.sms.satp.mapper.SceneCaseGroupMapper;
import com.sms.satp.repository.CustomizedSceneCaseRepository;
import com.sms.satp.repository.SceneCaseGroupRepository;
import com.sms.satp.service.impl.SceneCaseGroupServiceImpl;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

import static com.sms.satp.common.exception.ErrorCode.ADD_SCENE_CASE_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_SCENE_CASE_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_SCENE_CASE_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_SCENE_CASE_GROUP_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertTrue;

@DisplayName("Test cases for SceneCaseGroupServiceTest")
class SceneCaseGroupServiceTest {

    private final SceneCaseGroupRepository sceneCaseGroupRepository = mock(SceneCaseGroupRepository.class);
    private final SceneCaseGroupMapper sceneCaseGroupMapper = mock(SceneCaseGroupMapper.class);
    private final SceneCaseService sceneCaseService = mock(SceneCaseService.class);
    private final SceneCaseGroupService sceneCaseGroupService =
        new SceneCaseGroupServiceImpl(sceneCaseGroupRepository, sceneCaseGroupMapper, sceneCaseService);

    private final static String MOCK_ID = "1";
    private final static String MOCK_NAME = "name";

    @Test
    @DisplayName("Test the add method in the SceneCaseGroup service")
    void add_test() {
        SceneCaseGroup caseGroup = getGroup();
        when(sceneCaseGroupMapper.toSceneCaseGroupByAdd(any())).thenReturn(caseGroup);
        when(sceneCaseGroupRepository.insert(any(SceneCaseGroup.class))).thenReturn(caseGroup);
        AddSceneCaseGroupRequest request = AddSceneCaseGroupRequest.builder().name(MOCK_NAME).build();
        Boolean isSuccess = sceneCaseGroupService.add(request);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the add method in the SceneCaseGroup service thrown exception")
    void add_test_thrownException() {
        SceneCaseGroup caseGroup = getGroup();
        when(sceneCaseGroupMapper.toSceneCaseGroupByAdd(any())).thenReturn(caseGroup);
        when(sceneCaseGroupRepository.insert(any(SceneCaseGroup.class)))
            .thenThrow(new ApiTestPlatformException(ADD_SCENE_CASE_GROUP_ERROR));
        AddSceneCaseGroupRequest request = AddSceneCaseGroupRequest.builder().name(MOCK_NAME).build();
        assertThatThrownBy(() -> sceneCaseGroupService.add(request)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the edit method in the SceneCaseGroup service")
    void edit_test() {
        SceneCaseGroup caseGroup = getGroup();
        when(sceneCaseGroupMapper.toSceneCaseGroupByUpdate(any())).thenReturn(caseGroup);
        Optional<SceneCaseGroup> optional = Optional.ofNullable(caseGroup);
        when(sceneCaseGroupRepository.findById(any())).thenReturn(optional);
        when(sceneCaseGroupRepository.save(any())).thenReturn(caseGroup);
        Boolean isSuccess =
            sceneCaseGroupService.edit(UpdateSceneCaseGroupRequest.builder().id(MOCK_ID).name(MOCK_NAME).build());
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the edit method in the SceneCaseGroup service thrown exception")
    void edit_test_thrownException() {
        SceneCaseGroup caseGroup = getGroup();
        when(sceneCaseGroupMapper.toSceneCaseGroupByUpdate(any())).thenReturn(caseGroup);
        when(sceneCaseGroupRepository.findById(any()))
            .thenThrow(new ApiTestPlatformException(EDIT_SCENE_CASE_GROUP_ERROR));
        assertThatThrownBy(() -> sceneCaseGroupService
            .edit(UpdateSceneCaseGroupRequest.builder().id(MOCK_ID).name(MOCK_NAME).build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the deleteById method in the SceneCaseGroup service")
    void deleteById_test() {
        SceneCaseGroup caseGroup = getGroup();
        Optional<SceneCaseGroup> optional = Optional.ofNullable(caseGroup);
        when(sceneCaseGroupRepository.findById(any())).thenReturn(optional);
        doNothing().when(sceneCaseGroupRepository).deleteById(any());
        Page<SceneCaseResponse> sceneCasePage = mock(Page.class);
        when(sceneCasePage.getContent()).thenReturn(Lists.newArrayList(SceneCaseResponse.builder().id(MOCK_ID).build()));
        when(sceneCaseService.batchEdit(any())).thenReturn(Boolean.TRUE);
        Boolean isSuccess = sceneCaseGroupService.deleteById(MOCK_ID);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the deleteById method in the SceneCaseGroup service thrown exception")
    void deleteById_test_thrownException() {
        when(sceneCaseGroupRepository.findById(any()))
            .thenThrow(new ApiTestPlatformException(DELETE_SCENE_CASE_GROUP_ERROR));
        assertThatThrownBy(() -> sceneCaseGroupService.deleteById(MOCK_ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the getList method in the SceneCaseGroup service")
    void getList_test() {
        List<SceneCaseGroup> caseTemplateGroups =
            Lists.newArrayList(SceneCaseGroup.builder().id(MOCK_ID).build());
        when(sceneCaseGroupRepository.findAll(any(Example.class))).thenReturn(caseTemplateGroups);
        List<SceneCaseGroupResponse> caseTemplateGroupResponseList = Lists.newArrayList(
            SceneCaseGroupResponse.builder().id(MOCK_ID).build());
        when(sceneCaseGroupMapper.toResponseList(any())).thenReturn(caseTemplateGroupResponseList);
        List<SceneCaseGroupResponse> responses =
            sceneCaseGroupService.getList(SearchSceneCaseGroupRequest.builder().projectId(MOCK_ID).build());
        assertThat(responses).isNotEmpty();
    }

    @Test
    @DisplayName("Test the getList method in the SceneCaseGroup service thrown exception")
    void getList_test_thrownException() {
        when(sceneCaseGroupRepository.findAll(any(Example.class)))
            .thenThrow(new ApiTestPlatformException(GET_SCENE_CASE_GROUP_LIST_ERROR));
        assertThatThrownBy(
            () -> sceneCaseGroupService.getList(SearchSceneCaseGroupRequest.builder().projectId(MOCK_ID).build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    private SceneCaseGroup getGroup() {
        return SceneCaseGroup.builder().id(MOCK_ID).build();
    }


}
