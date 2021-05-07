package com.sms.satp.service;

import com.sms.satp.dto.AddSceneCaseApiDto;
import com.sms.satp.dto.SceneCaseApiDto;
import com.sms.satp.dto.UpdateSceneCaseApiSortOrderDto;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import java.util.List;

public interface SceneCaseApiService {

    void batch(AddSceneCaseApiDto addSceneCaseApiDto);

    void deleteById(String id);

    void edit(SceneCaseApiDto sceneCaseApiDto);

    void batchEdit(UpdateSceneCaseApiSortOrderDto updateSceneCaseApiSortOrderDto);

    List<SceneCaseApiDto> listBySceneCaseId(String sceneCaseId, boolean remove);

    List<SceneCaseApi> listBySceneCaseId(String sceneCaseId);

    SceneCaseApiDto getSceneCaseApiById(String id);
}
