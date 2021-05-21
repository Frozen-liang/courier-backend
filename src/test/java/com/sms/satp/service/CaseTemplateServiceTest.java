package com.sms.satp.service;

import com.google.common.collect.Lists;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.request.AddCaseTemplateRequest;
import com.sms.satp.dto.request.CaseTemplateSearchRequest;
import com.sms.satp.dto.request.UpdateCaseTemplateRequest;
import com.sms.satp.dto.response.CaseTemplateResponse;
import com.sms.satp.entity.scenetest.CaseTemplate;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import com.sms.satp.mapper.CaseTemplateMapper;
import com.sms.satp.repository.CaseTemplateRepository;
import com.sms.satp.repository.CustomizedCaseTemplateRepository;
import com.sms.satp.service.impl.CaseTemplateServiceImpl;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static com.sms.satp.common.exception.ErrorCode.ADD_CASE_TEMPLATE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_CASE_TEMPLATE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_CASE_TEMPLATE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_CASE_TEMPLATE_PAGE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.SEARCH_CASE_TEMPLATE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertTrue;

@DisplayName("Test cases for CaseTemplateServiceTest")
class CaseTemplateServiceTest {

    private CaseTemplateRepository caseTemplateRepository;
    private CustomizedCaseTemplateRepository customizedCaseTemplateRepository;
    private CaseTemplateMapper caseTemplateMapper;
    private CaseTemplateServiceImpl caseTemplateService;
    private CaseTemplateApiService caseTemplateApiService;

    private final static String MOCK_ID = new ObjectId().toString();
    private final static String MOCK_NAME = "test";
    private final static String MOCK_PROJECT_ID = "1";
    private final static String MOCK_GROUP_ID = "1";
    private final static Long MOCK_CREATE_USER_ID = 1L;
    private final static Integer MOCK_PAGE = 1;
    private final static Integer MOCK_SIZE = 1;
    private final static long MOCK_TOTAL = 1L;

    @BeforeEach
    void setBean() {
        caseTemplateRepository = mock(CaseTemplateRepository.class);
        customizedCaseTemplateRepository = mock(CustomizedCaseTemplateRepository.class);
        caseTemplateMapper = mock(CaseTemplateMapper.class);
        caseTemplateApiService = mock(CaseTemplateApiService.class);
        caseTemplateService = new CaseTemplateServiceImpl(caseTemplateRepository, customizedCaseTemplateRepository,
            caseTemplateMapper, caseTemplateApiService);
    }

    @Test
    @DisplayName("Test the add method in the CaseTemplate service")
    void add_test() {
        CaseTemplate caseTemplate =
            CaseTemplate.builder().name(MOCK_NAME).projectId(MOCK_PROJECT_ID).groupId(MOCK_GROUP_ID)
                .createUserId(MOCK_CREATE_USER_ID).build();
        when(caseTemplateMapper.toCaseTemplateByAddRequest(any())).thenReturn(caseTemplate);
        when(caseTemplateRepository.insert(any(CaseTemplate.class))).thenReturn(caseTemplate);
        Boolean isSuccess = caseTemplateService.add(AddCaseTemplateRequest.builder().build());
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the add method in the CaseTemplate service throws exception")
    void add_test_thenThrowException() {
        CaseTemplate caseTemplate =
            CaseTemplate.builder().name(MOCK_NAME).projectId(MOCK_PROJECT_ID).groupId(MOCK_GROUP_ID)
                .createUserId(MOCK_CREATE_USER_ID).build();
        when(caseTemplateMapper.toCaseTemplateByAddRequest(any())).thenReturn(caseTemplate);
        when(caseTemplateRepository.insert(any(CaseTemplate.class)))
            .thenThrow(new ApiTestPlatformException(ADD_CASE_TEMPLATE_ERROR));
        assertThatThrownBy(() -> caseTemplateService.add(AddCaseTemplateRequest.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the deleteByIds method in the CaseTemplate service")
    void deleteByIds_test() {
        doNothing().when(caseTemplateRepository).deleteById(any());
        List<CaseTemplateApi> caseTemplateApiList = Lists.newArrayList(CaseTemplateApi.builder().build());
        when(caseTemplateApiService.listByCaseTemplateId(any())).thenReturn(caseTemplateApiList);
        when(caseTemplateApiService.deleteByIds(any())).thenReturn(Boolean.TRUE);
        Boolean isSuccess = caseTemplateService.deleteByIds(Lists.newArrayList(MOCK_ID));
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the deleteById method in the CaseTemplate service throws exception")
    void deleteByIds_test_thenThrownException() {
        doThrow(new ApiTestPlatformException(DELETE_CASE_TEMPLATE_ERROR)).when(caseTemplateRepository)
            .deleteById(any());
        assertThatThrownBy(() -> caseTemplateService.deleteByIds(Lists.newArrayList(MOCK_ID)))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the edit method in the CaseTemplate service")
    void edit_test() {
        CaseTemplate caseTemplate =
            CaseTemplate.builder().id(MOCK_ID).modifyUserId(MOCK_CREATE_USER_ID).name(MOCK_NAME)
                .removed(Boolean.FALSE).build();
        when(caseTemplateMapper.toCaseTemplateByUpdateRequest(any())).thenReturn(caseTemplate);
        Optional<CaseTemplate> optionalSceneCase = Optional
            .ofNullable(CaseTemplate.builder().removed(Boolean.TRUE).build());
        when(caseTemplateRepository.findById(any())).thenReturn(optionalSceneCase);
        when(caseTemplateRepository.save(any(CaseTemplate.class))).thenReturn(caseTemplate);
        List<CaseTemplateApi> caseTemplateApiDtoList = Lists
            .newArrayList(CaseTemplateApi.builder().id(MOCK_ID).build());
        when(caseTemplateApiService.getApiByCaseTemplateId(any(), anyBoolean())).thenReturn(caseTemplateApiDtoList);
        when(caseTemplateApiService.editAll(any())).thenReturn(Boolean.TRUE);
        Boolean isSuccess = caseTemplateService.edit(UpdateCaseTemplateRequest.builder().removed(Boolean.FALSE).build());
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the edit method in the CaseTemplate service throws exception")
    void edit_test_thenThrownException() {
        CaseTemplate caseTemplate =
            CaseTemplate.builder().id(MOCK_ID).modifyUserId(MOCK_CREATE_USER_ID).name(MOCK_NAME)
                .removed(Boolean.FALSE).build();
        when(caseTemplateMapper.toCaseTemplate(any())).thenReturn(caseTemplate);
        Optional<CaseTemplate> optionalSceneCase = Optional
            .ofNullable(CaseTemplate.builder().removed(Boolean.TRUE).build());
        when(caseTemplateRepository.findById(any())).thenReturn(optionalSceneCase);
        when(caseTemplateRepository.save(any(CaseTemplate.class)))
            .thenThrow(new ApiTestPlatformException(EDIT_CASE_TEMPLATE_ERROR));
        assertThatThrownBy(() -> caseTemplateService.edit(UpdateCaseTemplateRequest.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the page method in the CaseTemplate service")
    void page_test() {
        List<CaseTemplate> dtoList = Lists.newArrayList(CaseTemplate.builder().build());
        Pageable pageable = PageRequest.of(MOCK_PAGE, MOCK_SIZE);
        Page<CaseTemplate> caseTemplatePage = new PageImpl<>(dtoList, pageable, MOCK_TOTAL);
        when(caseTemplateRepository.findAll(any(), (Pageable) any())).thenReturn(caseTemplatePage);
        Page<CaseTemplateResponse> pageDto = caseTemplateService.page(PageDto.builder().build(), MOCK_PROJECT_ID);
        assertThat(pageDto).isNotNull();
    }

    @Test
    @DisplayName("Test the page method in the CaseTemplate service thrown exception")
    void page_test_thenThrownException() {
        when(caseTemplateRepository.findAll(any(), (Pageable) any()))
            .thenThrow(new ApiTestPlatformException(GET_CASE_TEMPLATE_PAGE_ERROR));
        assertThatThrownBy(() -> caseTemplateService.page(PageDto.builder().build(), MOCK_PROJECT_ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the search method in the CaseTemplate service")
    void search_test() {
        CaseTemplateSearchRequest dto = new CaseTemplateSearchRequest();
        dto.setName(MOCK_NAME);
        List<CaseTemplate> dtoList = Lists.newArrayList(CaseTemplate.builder().build());
        Pageable pageable = PageRequest.of(MOCK_PAGE, MOCK_SIZE);
        Page<CaseTemplate> caseTemplatePage = new PageImpl<>(dtoList, pageable, MOCK_TOTAL);
        when(customizedCaseTemplateRepository.search(any(), any())).thenReturn(caseTemplatePage);
        Page<CaseTemplateResponse> pageDto = caseTemplateService.search(dto, MOCK_PROJECT_ID);
        assertThat(pageDto).isNotNull();
    }

    @Test
    @DisplayName("Test the search method in the CaseTemplate service thrown exception")
    void search_test_thenThrownException() {
        CaseTemplateSearchRequest dto = new CaseTemplateSearchRequest();
        dto.setName(MOCK_NAME);
        when(customizedCaseTemplateRepository.search(any(), any()))
            .thenThrow(new ApiTestPlatformException(SEARCH_CASE_TEMPLATE_ERROR));
        assertThatThrownBy(() -> caseTemplateService.search(dto, MOCK_PROJECT_ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }
}
