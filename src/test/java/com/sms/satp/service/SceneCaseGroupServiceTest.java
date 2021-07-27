package com.sms.satp.service;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.SceneCaseGroupRequest;
import com.sms.satp.dto.response.SceneCaseGroupResponse;
import com.sms.satp.dto.response.TreeResponse;
import com.sms.satp.entity.group.SceneCaseGroupEntity;
import com.sms.satp.entity.scenetest.SceneCaseEntity;
import com.sms.satp.mapper.SceneCaseGroupMapper;
import com.sms.satp.repository.CustomizedSceneCaseRepository;
import com.sms.satp.repository.SceneCaseGroupRepository;
import com.sms.satp.service.impl.SceneCaseGroupServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    private final CustomizedSceneCaseRepository customizedSceneCaseRepository = mock(
        CustomizedSceneCaseRepository.class);
    private final SceneCaseGroupService sceneCaseGroupService =
        new SceneCaseGroupServiceImpl(sceneCaseGroupRepository, sceneCaseGroupMapper, sceneCaseService,
            customizedSceneCaseRepository);

    private final static String MOCK_ID = "1";
    private final static String MOCK_ID_TWO = "2";
    private final static Integer MOCK_DEPTH = 1;
    private final static Integer MOCK_DEPTH_TWO = 2;
    private final static String MOCK_NAME = "name";

    @Test
    @DisplayName("Test the add method in the SceneCaseGroup service")
    void add_test() {
        SceneCaseGroupEntity caseGroup = getGroup();
        when(sceneCaseGroupMapper.toSceneCaseGroupEntity(any())).thenReturn(caseGroup);
        when(sceneCaseGroupRepository.insert(any(SceneCaseGroupEntity.class))).thenReturn(caseGroup);
        when(sceneCaseGroupRepository.findById(any())).thenReturn(Optional.ofNullable(caseGroup));
        SceneCaseGroupRequest request = SceneCaseGroupRequest.builder().name(MOCK_NAME).build();
        Boolean isSuccess = sceneCaseGroupService.add(request);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the add method in the SceneCaseGroup service thrown exception")
    void add_test_thrownException() {
        SceneCaseGroupEntity caseGroup = getGroup();
        when(sceneCaseGroupMapper.toSceneCaseGroupEntity(any())).thenReturn(caseGroup);
        when(sceneCaseGroupRepository.insert(any(SceneCaseGroupEntity.class)))
            .thenThrow(new ApiTestPlatformException(ADD_SCENE_CASE_GROUP_ERROR));
        SceneCaseGroupRequest request = SceneCaseGroupRequest.builder().name(MOCK_NAME).build();
        assertThatThrownBy(() -> sceneCaseGroupService.add(request)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the add method in the SceneCaseGroup service thrown exception")
    void add_test_thrown_Exception() {
        SceneCaseGroupEntity caseGroup = getGroup();
        when(sceneCaseGroupMapper.toSceneCaseGroupEntity(any())).thenReturn(caseGroup);
        when(sceneCaseGroupRepository.insert(any(SceneCaseGroupEntity.class)))
            .thenThrow(new RuntimeException());
        SceneCaseGroupRequest request = SceneCaseGroupRequest.builder().name(MOCK_NAME).build();
        assertThatThrownBy(() -> sceneCaseGroupService.add(request)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the edit method in the SceneCaseGroup service")
    void edit_test() {
        SceneCaseGroupEntity caseGroup = getGroup();
        when(sceneCaseGroupMapper.toSceneCaseGroupEntity(any())).thenReturn(caseGroup);
        Optional<SceneCaseGroupEntity> optional = Optional.ofNullable(caseGroup);
        when(sceneCaseGroupRepository.findById(any())).thenReturn(optional);
        when(sceneCaseGroupRepository.save(any())).thenReturn(caseGroup);
        Boolean isSuccess =
            sceneCaseGroupService.edit(SceneCaseGroupRequest.builder().id(MOCK_ID).name(MOCK_NAME).build());
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the edit method in the SceneCaseGroup service thrown exception")
    void edit_test_thrownException() {
        SceneCaseGroupEntity caseGroup = getGroup();
        when(sceneCaseGroupMapper.toSceneCaseGroupEntity(any())).thenReturn(caseGroup);
        when(sceneCaseGroupRepository.findById(any()))
            .thenThrow(new ApiTestPlatformException(EDIT_SCENE_CASE_GROUP_ERROR));
        assertThatThrownBy(() -> sceneCaseGroupService
            .edit(SceneCaseGroupRequest.builder().id(MOCK_ID).name(MOCK_NAME).build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the deleteById method in the SceneCaseGroup service")
    void deleteById_test() {
        SceneCaseGroupEntity caseGroup = getGroup();
        Optional<SceneCaseGroupEntity> optional = Optional.ofNullable(caseGroup);
        when(sceneCaseGroupRepository.findById(any())).thenReturn(optional);
        when(sceneCaseGroupRepository.findAllByPathContains(any())).thenReturn(
            Stream.of(SceneCaseGroupEntity.builder().id(MOCK_ID).build()));
        doNothing().when(sceneCaseGroupRepository).deleteAllByIdIn(any());
        List<SceneCaseEntity> sceneCaseList = Lists.newArrayList(SceneCaseEntity.builder().build());
        when(customizedSceneCaseRepository.getSceneCaseIdsByGroupIds(any())).thenReturn(sceneCaseList);
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
    @DisplayName("Test the deleteById method in the SceneCaseGroup service thrown exception")
    void deleteById_test_thrown_Exception() {
        when(sceneCaseGroupRepository.findById(any()))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> sceneCaseGroupService.deleteById(MOCK_ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the getList method in the SceneCaseGroup service")
    void getList_test() {
        List<SceneCaseGroupEntity> caseTemplateGroups =
            Lists.newArrayList(SceneCaseGroupEntity.builder().id(MOCK_ID).build());
        when(sceneCaseGroupRepository.findSceneCaseGroupEntitiesByProjectId(any())).thenReturn(caseTemplateGroups);
        List<SceneCaseGroupResponse> caseTemplateGroupResponseList = Lists.newArrayList(
            SceneCaseGroupResponse.builder().id(MOCK_ID).depth(MOCK_DEPTH).build(),
            SceneCaseGroupResponse.builder().id(MOCK_ID_TWO).parentId(MOCK_ID).depth(MOCK_DEPTH_TWO).build());
        when(sceneCaseGroupMapper.toResponse(any())).thenReturn(caseTemplateGroupResponseList);
        List<TreeResponse> responses = sceneCaseGroupService.list(MOCK_ID);
        assertThat(responses).isNotEmpty();
    }

    @Test
    @DisplayName("Test the getList method in the SceneCaseGroup service thrown exception")
    void getList_test_thrownException() {
        when(sceneCaseGroupRepository.findSceneCaseGroupEntitiesByProjectId(any()))
            .thenThrow(new ApiTestPlatformException(GET_SCENE_CASE_GROUP_LIST_ERROR));
        assertThatThrownBy(() -> sceneCaseGroupService.list(MOCK_ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    private SceneCaseGroupEntity getGroup() {
        return SceneCaseGroupEntity.builder().id(MOCK_ID).parentId(MOCK_ID_TWO).build();
    }


}
