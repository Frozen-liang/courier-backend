package com.sms.courier.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.sms.courier.dto.PageDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@DisplayName("Tests for PageDtoConverter")
public class PageDtoConverterTest {

    private final PageDto pageDto = mock(PageDto.class);
    private static final Integer FRONT_FIRST_NUMBER = 1;

    @Test
    @DisplayName("Test the frontMapping method in the PageDtoConverter")
    public void frontMapping_test() {
        PageDtoConverter.frontMapping(pageDto);
        verify(pageDto).setPageNumber(pageDto.getPageNumber() - FRONT_FIRST_NUMBER);
    }

    @Test
    @DisplayName("Test the createSort method in the PageDtoConverter")
    public void createSort_test() {
        PageDto partPageDto = PageDto.builder().order("desc").build();
        Sort result = Sort.by(Direction.fromString(partPageDto.getOrder()), partPageDto.getSort());
        assertThat(PageDtoConverter.createSort(partPageDto)).isEqualTo(result);
    }

    @Test
    @DisplayName("Test the createPageable method in the PageDtoConverter")
    public void createPageable_test() {
        PageDto partPageDto = PageDto.builder().order("desc").build();
        PageRequest result = PageRequest
            .of(partPageDto.getPageNumber(), partPageDto.getPageSize(), PageDtoConverter.createSort(partPageDto));
        assertThat(PageDtoConverter.createPageable(partPageDto)).isEqualTo(result);
    }
}
