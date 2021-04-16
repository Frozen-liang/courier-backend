package com.sms.satp.service;

import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.scenetest.AddSceneCaseDto;
import com.sms.satp.entity.scenetest.SceneCaseDto;
import com.sms.satp.entity.scenetest.SceneCaseSearchDto;
import com.sms.satp.entity.scenetest.UpdateSceneCaseDto;
import org.springframework.data.domain.Page;

public interface SceneCaseService {

    void add(AddSceneCaseDto sceneCaseDto);

    void deleteById(String id);

    void edit(UpdateSceneCaseDto sceneCaseDto);

    Page<SceneCaseDto> page(PageDto pageDto, String projectId);

    Page<SceneCaseDto> search(SceneCaseSearchDto searchDto, String projectId);
}
