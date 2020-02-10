package com.sms.satp.service;

import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.StatusCodeDocDto;
import org.springframework.data.domain.Page;

public interface StatusCodeDocService {

    Page<StatusCodeDocDto> page(PageDto pageDto, String projectId);

    void add(StatusCodeDocDto projectEnvironmentDto);

    void edit(StatusCodeDocDto projectEnvironmentDto);

    void deleteById(String id);
}
