package com.sms.satp.service;

import com.sms.satp.entity.dto.AddSceneCaseApiDto;
import com.sms.satp.entity.dto.SceneCaseApiDto;
import com.sms.satp.entity.dto.UpdateSceneCaseApiDto;
import com.sms.satp.entity.dto.UpdateSceneCaseApiSortOrderDto;
import java.util.List;

public interface SceneCaseApiService {

    void batch(AddSceneCaseApiDto addSceneCaseApiDto);

    void deleteById(String id);

    void edit(UpdateSceneCaseApiDto updateSceneCaseApiDto);

    void editSortOrder(UpdateSceneCaseApiSortOrderDto updateSceneCaseApiSortOrderDto);

    List<SceneCaseApiDto> listBySceneCaseId(String sceneCaseId);

    SceneCaseApiDto getSceneCaseApiById(String id);
}
