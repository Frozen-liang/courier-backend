package com.sms.satp.service;

import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.SceneCaseApiLogDto;
import org.springframework.data.domain.Page;

public interface SceneCaseApiLogService {

    Page<SceneCaseApiLogDto> page(PageDto pageDto, String projectId);

    void add(SceneCaseApiLogDto sceneCaseApiLogDto);
}
