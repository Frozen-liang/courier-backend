package com.sms.satp.service;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.CaseTemplateGroupRequest;
import com.sms.satp.dto.request.SearchCaseTemplateGroupRequest;
import com.sms.satp.dto.response.CaseTemplateGroupResponse;
import com.sms.satp.dto.response.TreeResponse;
import com.sms.satp.entity.group.CaseTemplateGroupEntity;
import com.sms.satp.entity.scenetest.CaseTemplateEntity;
import com.sms.satp.mapper.CaseTemplateGroupMapper;
import com.sms.satp.repository.CaseTemplateGroupRepository;
import com.sms.satp.repository.CustomizedCaseTemplateRepository;
import com.sms.satp.service.impl.CaseTemplateGroupServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;

import static com.sms.satp.common.exception.ErrorCode.ADD_CASE_TEMPLATE_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_CASE_TEMPLATE_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_CASE_TEMPLATE_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_CASE_TEMPLATE_GROUP_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertTrue;

@DisplayName("Test cases for CaseTemplateGroupServiceTest")
class CaseTemplateGroupServiceTest {

    private final CaseTemplateGroupRepository caseTemplateGroupRepository = mock(CaseTemplateGroupRepository.class);
    private final CaseTemplateGroupMapper caseTemplateGroupMapper = mock(CaseTemplateGroupMapper.class);
    private final CaseTemplateService caseTemplateService = mock(CaseTemplateService.class);
    private final CustomizedCaseTemplateRepository customizedCaseTemplateRepository =
        mock(CustomizedCaseTemplateRepository.class);

    private final CaseTemplateGroupService caseTemplateGroupService =
        new CaseTemplateGroupServiceImpl(caseTemplateGroupRepository, caseTemplateGroupMapper, caseTemplateService,
            customizedCaseTemplateRepository);

    private final static String MOCK_ID = "1";
    private final static String MOCK_ID_TWO = "2";
    private final static Integer MOCK_DEPTH = 1;
    private final static Integer MOCK_DEPTH_TWO = 2;
    private final static String MOCK_NAME = "name";

    @Test
    @DisplayName("Test the add method in the CaseTemplateGroup service")
    void add_test() {
        CaseTemplateGroupEntity caseGroup = getGroup();
        when(caseTemplateGroupMapper.toCaseTemplateGroupEntity(any())).thenReturn(caseGroup);
        when(caseTemplateGroupRepository.insert(any(CaseTemplateGroupEntity.class))).thenReturn(caseGroup);
        when(caseTemplateGroupRepository.findById(any())).thenReturn(Optional.ofNullable(caseGroup));
        CaseTemplateGroupRequest request = CaseTemplateGroupRequest.builder().name(MOCK_NAME).build();
        Boolean isSuccess = caseTemplateGroupService.add(request);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the add method in the CaseTemplateGroup service thrown exception")
    void add_test_thrownException() {
        CaseTemplateGroupEntity caseGroup = getGroup();
        when(caseTemplateGroupMapper.toCaseTemplateGroupEntity(any())).thenReturn(caseGroup);
        when(caseTemplateGroupRepository.insert(any(CaseTemplateGroupEntity.class)))
            .thenThrow(new ApiTestPlatformException(ADD_CASE_TEMPLATE_GROUP_ERROR));
        CaseTemplateGroupRequest request = CaseTemplateGroupRequest.builder().name(MOCK_NAME).build();
        assertThatThrownBy(() -> caseTemplateGroupService.add(request)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the edit method in the CaseTemplateGroup service")
    void edit_test() {
        CaseTemplateGroupEntity caseGroup = getGroup();
        when(caseTemplateGroupMapper.toCaseTemplateGroupEntity(any())).thenReturn(caseGroup);
        Optional<CaseTemplateGroupEntity> optional = Optional.ofNullable(caseGroup);
        when(caseTemplateGroupRepository.findById(any())).thenReturn(optional);
        when(caseTemplateGroupRepository.save(any())).thenReturn(caseGroup);
        Boolean isSuccess =
            caseTemplateGroupService.edit(CaseTemplateGroupRequest.builder().id(MOCK_ID).name(MOCK_NAME).build());
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the edit method in the CaseTemplateGroup service thrown exception")
    void edit_test_thrownException() {
        CaseTemplateGroupEntity caseGroup = getGroup();
        when(caseTemplateGroupMapper.toCaseTemplateGroupEntity(any())).thenReturn(caseGroup);
        when(caseTemplateGroupRepository.findById(any()))
            .thenThrow(new ApiTestPlatformException(EDIT_CASE_TEMPLATE_GROUP_ERROR));
        assertThatThrownBy(() -> caseTemplateGroupService
            .edit(CaseTemplateGroupRequest.builder().id(MOCK_ID).name(MOCK_NAME).build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the deleteById method in the CaseTemplateGroup service")
    void deleteById_test() {
        CaseTemplateGroupEntity caseGroup = getGroup();
        Optional<CaseTemplateGroupEntity> optional = Optional.ofNullable(caseGroup);
        when(caseTemplateGroupRepository.findById(any())).thenReturn(optional);
        when(caseTemplateGroupRepository.findAllByPathContains(any()))
            .thenReturn(Stream.of(CaseTemplateGroupEntity.builder().id(MOCK_ID).build()));
        List<CaseTemplateEntity> caseTemplatePage = Lists
            .newArrayList(CaseTemplateEntity.builder().id(MOCK_ID).build());
        when(customizedCaseTemplateRepository.getCaseTemplateIdsByGroupIds(any())).thenReturn(caseTemplatePage);
        Boolean isSuccess = caseTemplateGroupService.deleteById(MOCK_ID);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the deleteById method in the CaseTemplateGroup service thrown exception")
    void deleteById_test_thrownException() {
        when(caseTemplateGroupRepository.findById(any()))
            .thenThrow(new ApiTestPlatformException(DELETE_CASE_TEMPLATE_GROUP_ERROR));
        assertThatThrownBy(() -> caseTemplateGroupService.deleteById(MOCK_ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the getList method in the CaseTemplateGroup service")
    void getList_test() {
        List<CaseTemplateGroupEntity> caseTemplateGroups =
            Lists.newArrayList(CaseTemplateGroupEntity.builder().id(MOCK_ID).build());
        when(caseTemplateGroupRepository.findCaseTemplateGroupEntitiesByProjectId(any()))
            .thenReturn(caseTemplateGroups);
        List<CaseTemplateGroupResponse> caseTemplateGroupResponseList = Lists.newArrayList(
            CaseTemplateGroupResponse.builder().id(MOCK_ID).depth(MOCK_DEPTH).build(),
            CaseTemplateGroupResponse.builder().id(MOCK_ID_TWO).parentId(MOCK_ID).depth(MOCK_DEPTH_TWO).build());
        when(caseTemplateGroupMapper.toResponse(any())).thenReturn(caseTemplateGroupResponseList);
        List<TreeResponse> responses = caseTemplateGroupService.list(MOCK_ID);
        assertThat(responses).isNotEmpty();
    }

    @Test
    @DisplayName("Test the getList method in the CaseTemplateGroup service thrown exception")
    void getList_test_thrownException() {
        when(caseTemplateGroupRepository.findCaseTemplateGroupEntitiesByProjectId(any()))
            .thenThrow(new ApiTestPlatformException(GET_CASE_TEMPLATE_GROUP_LIST_ERROR));
        assertThatThrownBy(() -> caseTemplateGroupService.list(MOCK_ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    private CaseTemplateGroupEntity getGroup() {
        return CaseTemplateGroupEntity.builder().id(MOCK_ID).parentId(MOCK_ID_TWO).build();
    }

}
