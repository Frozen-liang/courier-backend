package com.sms.satp.service;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.AddCaseTemplateGroupRequest;
import com.sms.satp.dto.request.SearchCaseTemplateGroupRequest;
import com.sms.satp.dto.request.UpdateCaseTemplateGroupRequest;
import com.sms.satp.dto.response.CaseTemplateGroupResponse;
import com.sms.satp.entity.group.CaseTemplateGroup;
import com.sms.satp.entity.scenetest.CaseTemplate;
import com.sms.satp.mapper.CaseTemplateGroupMapper;
import com.sms.satp.repository.CaseTemplateGroupRepository;
import com.sms.satp.repository.CustomizedCaseTemplateRepository;
import com.sms.satp.service.impl.CaseTemplateGroupServiceImpl;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

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

    private final CaseTemplateGroupService caseTemplateGroupService =
        new CaseTemplateGroupServiceImpl(caseTemplateGroupRepository, caseTemplateGroupMapper, caseTemplateService);

    private final static String MOCK_ID = "1";
    private final static String MOCK_NAME = "name";

    @Test
    @DisplayName("Test the add method in the CaseTemplateGroup service")
    void add_test() {
        CaseTemplateGroup caseGroup = getGroup();
        when(caseTemplateGroupMapper.toCaseTemplateGroupByAdd(any())).thenReturn(caseGroup);
        when(caseTemplateGroupRepository.insert(any(CaseTemplateGroup.class))).thenReturn(caseGroup);
        AddCaseTemplateGroupRequest request = AddCaseTemplateGroupRequest.builder().name(MOCK_NAME).build();
        Boolean isSuccess = caseTemplateGroupService.add(request);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the add method in the CaseTemplateGroup service thrown exception")
    void add_test_thrownException() {
        CaseTemplateGroup caseGroup = getGroup();
        when(caseTemplateGroupMapper.toCaseTemplateGroupByAdd(any())).thenReturn(caseGroup);
        when(caseTemplateGroupRepository.insert(any(CaseTemplateGroup.class)))
            .thenThrow(new ApiTestPlatformException(ADD_CASE_TEMPLATE_GROUP_ERROR));
        AddCaseTemplateGroupRequest request = AddCaseTemplateGroupRequest.builder().name(MOCK_NAME).build();
        assertThatThrownBy(() -> caseTemplateGroupService.add(request)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the edit method in the CaseTemplateGroup service")
    void edit_test() {
        CaseTemplateGroup caseGroup = getGroup();
        when(caseTemplateGroupMapper.toCaseTemplateGroupByUpdate(any())).thenReturn(caseGroup);
        Optional<CaseTemplateGroup> optional = Optional.ofNullable(caseGroup);
        when(caseTemplateGroupRepository.findById(any())).thenReturn(optional);
        when(caseTemplateGroupRepository.save(any())).thenReturn(caseGroup);
        Boolean isSuccess =
            caseTemplateGroupService.edit(UpdateCaseTemplateGroupRequest.builder().id(MOCK_ID).name(MOCK_NAME).build());
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the edit method in the CaseTemplateGroup service thrown exception")
    void edit_test_thrownException() {
        CaseTemplateGroup caseGroup = getGroup();
        when(caseTemplateGroupMapper.toCaseTemplateGroupByUpdate(any())).thenReturn(caseGroup);
        when(caseTemplateGroupRepository.findById(any()))
            .thenThrow(new ApiTestPlatformException(EDIT_CASE_TEMPLATE_GROUP_ERROR));
        assertThatThrownBy(() -> caseTemplateGroupService
            .edit(UpdateCaseTemplateGroupRequest.builder().id(MOCK_ID).name(MOCK_NAME).build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the deleteById method in the CaseTemplateGroup service")
    void deleteById_test() {
        CaseTemplateGroup caseGroup = getGroup();
        Optional<CaseTemplateGroup> optional = Optional.ofNullable(caseGroup);
        when(caseTemplateGroupRepository.findById(any())).thenReturn(optional);
        doNothing().when(caseTemplateGroupRepository).deleteById(any());
        List<CaseTemplate> caseTemplatePage = Lists.newArrayList(CaseTemplate.builder().id(MOCK_ID).build());
        when(caseTemplateService.get(any(), any())).thenReturn(caseTemplatePage);
        when(caseTemplateService.batchEdit(any())).thenReturn(Boolean.TRUE);
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
        List<CaseTemplateGroup> caseTemplateGroups =
            Lists.newArrayList(CaseTemplateGroup.builder().id(MOCK_ID).build());
        when(caseTemplateGroupRepository.findAll(any(Example.class))).thenReturn(caseTemplateGroups);
        List<CaseTemplateGroupResponse> caseTemplateGroupResponseList = Lists.newArrayList(
            CaseTemplateGroupResponse.builder().id(MOCK_ID).build());
        when(caseTemplateGroupMapper.toResponseList(any())).thenReturn(caseTemplateGroupResponseList);
        List<CaseTemplateGroupResponse> responses =
            caseTemplateGroupService.getList(SearchCaseTemplateGroupRequest.builder().projectId(MOCK_ID).build());
        assertThat(responses).isNotEmpty();
    }

    @Test
    @DisplayName("Test the getList method in the CaseTemplateGroup service thrown exception")
    void getList_test_thrownException() {
        when(caseTemplateGroupRepository.findAll(any(Example.class)))
            .thenThrow(new ApiTestPlatformException(GET_CASE_TEMPLATE_GROUP_LIST_ERROR));
        assertThatThrownBy(
            () -> caseTemplateGroupService.getList(SearchCaseTemplateGroupRequest.builder().projectId(MOCK_ID).build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    private CaseTemplateGroup getGroup() {
        return CaseTemplateGroup.builder().id(MOCK_ID).build();
    }

}
