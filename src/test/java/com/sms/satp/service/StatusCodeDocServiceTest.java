package com.sms.satp.service;

import static com.sms.satp.common.ErrorCode.ADD_STATUS_CODE_DOC_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_STATUS_CODE_DOC_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_STATUS_CODE_DOC_ERROR;
import static com.sms.satp.common.ErrorCode.GET_STATUS_CODE_DOC_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_STATUS_CODE_DOC_PAGE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.ApplicationTests;
import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.StatusCodeDoc;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.StatusCodeDocDto;
import com.sms.satp.mapper.StatusCodeDocMapper;
import com.sms.satp.repository.StatusCodeDocRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@SpringBootTest(classes = ApplicationTests.class)
@DisplayName("Test cases for StatusCodeDocService")
class StatusCodeDocServiceTest {

    @MockBean
    private StatusCodeDocRepository statusCodeDocRepository;

    @SpyBean
    private StatusCodeDocService statusCodeDocService;
    
    @SpyBean
    private StatusCodeDocMapper statusCodeDocMapper;

    private final static int TOTAL_ELEMENTS = 60;
    private final static int PAGE_NUMBER = 2;
    private final static int PAGE_SIZE = 20;
    private final static int DEFAULT_PAGE_NUMBER = 0;
    private final static int DEFAULT_PAGE_SIZE = 10;
    private static final int FRONT_FIRST_NUMBER = 1;
    private final static String ID = "25";
    private final static String NOT_EXIST_ID = "30";
    private final static String PROJECT_ID = "25";
    private final static String CODE = "200";

    @Test
    @DisplayName("Test the paging method with no parameters in the StatusCodeDoc service")
    void page_default_test() {
        StatusCodeDoc statusCodeDoc = StatusCodeDoc.builder()
            .projectId(PROJECT_ID)
            .build();
        Example<StatusCodeDoc> example = Example.of(statusCodeDoc);
        PageDto pageDto = PageDto.builder().build();
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(pageDto.getPageNumber() - FRONT_FIRST_NUMBER, pageDto.getPageSize(), sort);
        List<StatusCodeDoc> statusCodeDocList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            statusCodeDocList.add(StatusCodeDoc.builder().code(CODE).build());
        }
        Page<StatusCodeDoc> statusCodeDocPage = new PageImpl<>(statusCodeDocList, pageable, TOTAL_ELEMENTS);
        when(statusCodeDocRepository.findAll(example, pageable)).thenReturn(statusCodeDocPage);
        Page<StatusCodeDocDto> statusCodeDocDtoPage = statusCodeDocService.page(pageDto, PROJECT_ID);
        assertThat(statusCodeDocDtoPage.getTotalElements()).isEqualTo(TOTAL_ELEMENTS);
        assertThat(statusCodeDocDtoPage.getPageable().getPageNumber()).isEqualTo(DEFAULT_PAGE_NUMBER);
        assertThat(statusCodeDocDtoPage.getPageable().getPageSize()).isEqualTo(DEFAULT_PAGE_SIZE);
        assertThat(statusCodeDocDtoPage.getContent()).allMatch(projectDto -> StringUtils
            .equals(projectDto.getCode(), CODE));
    }

    @Test
    @DisplayName("Test the paging method with specified parameters in the StatusCodeDoc service")
    void page_test() {
        StatusCodeDoc statusCodeDoc = StatusCodeDoc.builder()
            .projectId(PROJECT_ID)
            .build();
        Example<StatusCodeDoc> example = Example.of(statusCodeDoc);
        PageDto pageDto = PageDto.builder()
            .pageNumber(PAGE_NUMBER)
            .pageSize(PAGE_SIZE)
            .order("asc")
            .build();
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(pageDto.getPageNumber() - FRONT_FIRST_NUMBER, pageDto.getPageSize(), sort);
        List<StatusCodeDoc> statusCodeDocList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            statusCodeDocList.add(StatusCodeDoc.builder().code(CODE).build());
        }
        Page<StatusCodeDoc> statusCodeDocPage = new PageImpl<>(statusCodeDocList, pageable, TOTAL_ELEMENTS);
        when(statusCodeDocRepository.findAll(example, pageable)).thenReturn(statusCodeDocPage);
        Page<StatusCodeDocDto> statusCodeDocDtoPage = statusCodeDocService.page(pageDto, PROJECT_ID);
        assertThat(statusCodeDocDtoPage.getTotalElements()).isEqualTo(TOTAL_ELEMENTS);
        assertThat(statusCodeDocDtoPage.getPageable().getPageNumber()).isEqualTo(PAGE_NUMBER - FRONT_FIRST_NUMBER);
        assertThat(statusCodeDocDtoPage.getPageable().getPageSize()).isEqualTo(PAGE_SIZE);
        assertThat(statusCodeDocDtoPage.getContent()).allMatch(projectDto -> StringUtils.equals(projectDto.getCode(), CODE));
    }

    @Test
    @DisplayName("Test the add method in the StatusCodeDoc service")
    void add_test() {
        StatusCodeDocDto statusCodeDocDto = StatusCodeDocDto.builder().build();
        StatusCodeDoc statusCodeDoc = statusCodeDocMapper.toEntity(statusCodeDocDto);
        when(statusCodeDocRepository.insert(statusCodeDoc)).thenReturn(statusCodeDoc);
        statusCodeDocService.add(statusCodeDocDto);
        verify(statusCodeDocRepository, times(1)).insert(any(StatusCodeDoc.class));
    }

    @Test
    @DisplayName("Test the edit method in the StatusCodeDoc service")
    void edit_test() {
        StatusCodeDocDto statusCodeDocDto = StatusCodeDocDto.builder().id(ID).build();
        StatusCodeDoc statusCodeDoc = statusCodeDocMapper.toEntity(statusCodeDocDto);
        when(statusCodeDocRepository.findById(ID)).thenReturn(Optional.of(StatusCodeDoc.builder().build()));
        when(statusCodeDocRepository.save(statusCodeDoc)).thenReturn(statusCodeDoc);
        statusCodeDocService.edit(statusCodeDocDto);
        verify(statusCodeDocRepository, times(1)).save(any(StatusCodeDoc.class));
    }

    @Test
    @DisplayName("Test the method of querying the StatusCodeDoc by id")
    void findById_test() {
        StatusCodeDoc statusCodeDoc = StatusCodeDoc.builder()
            .code(CODE)
            .build();
        Optional<StatusCodeDoc> statusCodeDocOptional = Optional.ofNullable(statusCodeDoc);
        when(statusCodeDocRepository.findById(ID)).thenReturn(statusCodeDocOptional);
        StatusCodeDocDto result1 = statusCodeDocService.findById(ID);
        StatusCodeDocDto result2 = statusCodeDocService.findById(NOT_EXIST_ID);
        assertThat(result1.getCode()).isEqualTo(CODE);
        assertThat(result2).isNull();
    }

    @Test
    @DisplayName("Test the delete method in the StatusCodeDoc service")
    void delete_test() {
        doNothing().when(statusCodeDocRepository).deleteById(PROJECT_ID);
        statusCodeDocService.deleteById(PROJECT_ID);
        verify(statusCodeDocRepository, times(1)).deleteById(PROJECT_ID);
    }

    @Test
    @DisplayName("An exception occurred while getting statusCodeDoc page")
    void page_exception_test() {
        StatusCodeDoc statusCodeDoc = StatusCodeDoc.builder().projectId(PROJECT_ID).build();
        PageDto pageDto = PageDto.builder().build();
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(
            pageDto.getPageNumber(), pageDto.getPageSize(), sort);
        doThrow(new RuntimeException()).when(statusCodeDocRepository).findAll(Example.of(statusCodeDoc), pageable);
        assertThatThrownBy(() -> statusCodeDocService.page(pageDto, PROJECT_ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_STATUS_CODE_DOC_PAGE_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while adding statusCodeDoc")
    void add_exception_test() {
        doThrow(new RuntimeException()).when(statusCodeDocRepository).insert(any(StatusCodeDoc.class));
        assertThatThrownBy(() -> statusCodeDocService.add(StatusCodeDocDto.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_STATUS_CODE_DOC_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while edit statusCodeDoc")
    void edit_exception_test() {
        when(statusCodeDocRepository.findById(ID)).thenReturn(Optional.of(StatusCodeDoc.builder().build()));
        doThrow(new RuntimeException()).when(statusCodeDocRepository).save(argThat(t -> true));
        assertThatThrownBy(() -> statusCodeDocService.edit(StatusCodeDocDto.builder().id(ID).build()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_STATUS_CODE_DOC_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while getting statusCodeDoc by id")
    void getStatusCodeDoc_exception_test() {
        doThrow(new RuntimeException()).when(statusCodeDocRepository).findById(anyString());
        assertThatThrownBy(() -> statusCodeDocService.findById(anyString()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_STATUS_CODE_DOC_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while delete statusCodeDoc")
    void delete_exception_test() {
        doThrow(new RuntimeException()).when(statusCodeDocRepository).deleteById(anyString());
        assertThatThrownBy(() -> statusCodeDocService.deleteById(anyString()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_STATUS_CODE_DOC_BY_ID_ERROR.getCode());
    }

}