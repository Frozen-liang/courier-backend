package com.sms.satp.utils;

import com.sms.satp.entity.dto.PageDto;

public abstract class PageDtoConverter {

    private static final Integer FRONT_FIRST_NUMBER = 1;

    private PageDtoConverter() {
    }

    public static void frontMapping(PageDto pageDto) {
        pageDto.setPageNumber(pageDto.getPageNumber() - FRONT_FIRST_NUMBER);
    }
}
  