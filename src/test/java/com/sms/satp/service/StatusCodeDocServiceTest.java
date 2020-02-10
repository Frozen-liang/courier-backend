package com.sms.satp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.entity.StatusCodeDoc;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.StatusCodeDocDto;
import com.sms.satp.mapper.StatusCodeDocMapper;
import com.sms.satp.repository.ApiInterfaceRepository;
import com.sms.satp.repository.ProjectEnvironmentRepository;
import com.sms.satp.repository.ProjectRepository;
import com.sms.satp.repository.StatusCodeDocRepository;
import com.sms.satp.repository.WikiRepository;
import java.util.ArrayList;
import java.util.List;
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

@SpringBootTest(classes = ApplicationTests.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Test the service layer interface of the StatusCodeDoc")
public class StatusCodeDocServiceTest {

    @MockBean
    private ApiInterfaceRepository apiInterfaceRepository;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private ProjectEnvironmentRepository projectEnvironmentRepository;

    @MockBean
    private StatusCodeDocRepository statusCodeDocRepository;

    @MockBean
    private WikiRepository wikiRepository;
    
    @SpyBean
    private StatusCodeDocService statusCodeDocService;
    
    @SpyBean
    private StatusCodeDocMapper statusCodeDocMapper;

    private final static int TOTAL_ELEMENTS = 60;
    private final static int PAGE_NUMBER = 2;
    private final static int PAGE_SIZE = 20;
    private final static int DEFAULT_PAGE_NUMBER = 0;
    private final static int DEFAULT_PAGE_SIZE = 10;
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
        Pageable pageable = PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize(), sort);
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
        Pageable pageable = PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize(), sort);
        List<StatusCodeDoc> statusCodeDocList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            statusCodeDocList.add(StatusCodeDoc.builder().code(CODE).build());
        }
        Page<StatusCodeDoc> statusCodeDocPage = new PageImpl<>(statusCodeDocList, pageable, TOTAL_ELEMENTS);
        when(statusCodeDocRepository.findAll(example, pageable)).thenReturn(statusCodeDocPage);
        Page<StatusCodeDocDto> statusCodeDocDtoPage = statusCodeDocService.page(pageDto, PROJECT_ID);
        assertThat(statusCodeDocDtoPage.getTotalElements()).isEqualTo(TOTAL_ELEMENTS);
        assertThat(statusCodeDocDtoPage.getPageable().getPageNumber()).isEqualTo(PAGE_NUMBER);
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
        verify(statusCodeDocRepository, times(1)).insert(statusCodeDoc);
    }

    @Test
    @DisplayName("Test the edit method in the StatusCodeDoc service")
    void edit_test() {
        StatusCodeDocDto statusCodeDocDto = StatusCodeDocDto.builder().build();
        StatusCodeDoc statusCodeDoc = statusCodeDocMapper.toEntity(statusCodeDocDto);
        when(statusCodeDocRepository.save(statusCodeDoc)).thenReturn(statusCodeDoc);
        statusCodeDocService.edit(statusCodeDocDto);
        verify(statusCodeDocRepository, times(1)).save(statusCodeDoc);
    }

    @Test
    @DisplayName("Test the delete method in the StatusCodeDoc service")
    void delete_test() {
        doNothing().when(statusCodeDocRepository).deleteById(PROJECT_ID);
        statusCodeDocService.deleteById(PROJECT_ID);
        verify(statusCodeDocRepository, times(1)).deleteById(PROJECT_ID);
    }

}