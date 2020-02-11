package com.sms.satp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.ApplicationTests;
import com.sms.satp.entity.Wiki;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.WikiDto;
import com.sms.satp.mapper.WikiMapper;
import com.sms.satp.repository.WikiRepository;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
@DisplayName("Test the service layer interface of the Wiki")
class WikiServiceTest {


    @MockBean
    private WikiRepository wikiRepository;
    
    @SpyBean
    private WikiService wikiService;
    
    @Autowired
    private WikiMapper wikiMapper;

    private final static int TOTAL_ELEMENTS = 60;
    private final static int PAGE_NUMBER = 2;
    private final static int PAGE_SIZE = 20;
    private final static int DEFAULT_PAGE_NUMBER = 0;
    private final static int DEFAULT_PAGE_SIZE = 10;
    private final static String PROJECT_ID = "25";
    private final static String TITLE = "title";

    @Test
    @DisplayName("Test the paging method with no parameters in the Wiki service")
    void page_default_test() {
        Wiki wiki = Wiki.builder()
            .projectId(PROJECT_ID)
            .build();
        Example<Wiki> example = Example.of(wiki);
        PageDto pageDto = PageDto.builder().build();
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize(), sort);
        List<Wiki> wikiList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            wikiList.add(Wiki.builder().title(TITLE).build());
        }
        Page<Wiki> wikiPage = new PageImpl<>(wikiList, pageable, TOTAL_ELEMENTS);
        when(wikiRepository.findAll(example, pageable)).thenReturn(wikiPage);
        Page<WikiDto> wikiDtoPage = wikiService.page(pageDto, PROJECT_ID);
        assertThat(wikiDtoPage.getTotalElements()).isEqualTo(TOTAL_ELEMENTS);
        assertThat(wikiDtoPage.getPageable().getPageNumber()).isEqualTo(DEFAULT_PAGE_NUMBER);
        assertThat(wikiDtoPage.getPageable().getPageSize()).isEqualTo(DEFAULT_PAGE_SIZE);
        assertThat(wikiDtoPage.getContent()).allMatch(wikiDto -> StringUtils
            .equals(wikiDto.getTitle(), TITLE));
    }

    @Test
    @DisplayName("Test the paging method with specified parameters in the Wiki service")
    void page_test() {
        Wiki wiki = Wiki.builder()
            .projectId(PROJECT_ID)
            .build();
        Example<Wiki> example = Example.of(wiki);
        PageDto pageDto = PageDto.builder()
            .pageNumber(PAGE_NUMBER)
            .pageSize(PAGE_SIZE)
            .order("asc")
            .build();
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize(), sort);
        List<Wiki> wikiList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            wikiList.add(Wiki.builder().title(TITLE).build());
        }
        Page<Wiki> wikiPage = new PageImpl<>(wikiList, pageable, TOTAL_ELEMENTS);
        when(wikiRepository.findAll(example, pageable)).thenReturn(wikiPage);
        Page<WikiDto> wikiDtoPage = wikiService.page(pageDto, PROJECT_ID);
        assertThat(wikiDtoPage.getTotalElements()).isEqualTo(TOTAL_ELEMENTS);
        assertThat(wikiDtoPage.getPageable().getPageNumber()).isEqualTo(PAGE_NUMBER);
        assertThat(wikiDtoPage.getPageable().getPageSize()).isEqualTo(PAGE_SIZE);
        assertThat(wikiDtoPage.getContent()).allMatch(wikiDto -> StringUtils.equals(wikiDto.getTitle(),
            TITLE));
    }

    @Test
    @DisplayName("Test the add method in the Wiki service")
    void add_test() {
        WikiDto wikiDto = WikiDto.builder().build();
        Wiki wiki = wikiMapper.toEntity(wikiDto);
        when(wikiRepository.insert(wiki)).thenReturn(wiki);
        wikiService.add(wikiDto);
        verify(wikiRepository, times(1)).insert(wiki);
    }

    @Test
    @DisplayName("Test the edit method in the Wiki service")
    void edit_test() {
        WikiDto wikiDto = WikiDto.builder().build();
        Wiki wiki = wikiMapper.toEntity(wikiDto);
        when(wikiRepository.save(wiki)).thenReturn(wiki);
        wikiService.edit(wikiDto);
        verify(wikiRepository, times(1)).save(wiki);
    }

    @Test
    @DisplayName("Test the delete method in the Wiki service")
    void delete_test() {
        doNothing().when(wikiRepository).deleteById(PROJECT_ID);
        wikiService.deleteById(PROJECT_ID);
        verify(wikiRepository, times(1)).deleteById(PROJECT_ID);
    }

}