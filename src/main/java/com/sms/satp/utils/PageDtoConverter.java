package com.sms.satp.utils;

import com.sms.satp.dto.PageDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public abstract class PageDtoConverter {

    private static final Integer FRONT_FIRST_NUMBER = 1;

    private PageDtoConverter() {
    }

    public static void frontMapping(PageDto pageDto) {
        pageDto.setPageNumber(pageDto.getPageNumber() - FRONT_FIRST_NUMBER);
    }

    public static Sort createSort(PageDto pageDto) {
        return Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
    }

    public static Pageable createPageable(PageDto pageDto) {
        return PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize(), createSort(pageDto));
    }
}
  