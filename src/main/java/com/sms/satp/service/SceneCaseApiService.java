package com.sms.satp.service;

import com.sms.satp.entity.dto.AddSceneCaseApiDto;
import com.sms.satp.entity.dto.SceneCaseApiDto;
import com.sms.satp.entity.dto.UpdateSceneCaseApiDto;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import java.util.List;

public interface SceneCaseApiService {

    void batch(AddSceneCaseApiDto addSceneCaseApiDto);

    void deleteById(String id);

    void edit(SceneCaseApiDto sceneCaseApiDto);

    void batchEdit(UpdateSceneCaseApiDto updateSceneCaseApiSortOrderDto);

    List<SceneCaseApiDto> listBySceneCaseId(String sceneCaseId, boolean remove);

    List<SceneCaseApi> listBySceneCaseId(String sceneCaseId);

    SceneCaseApiDto getSceneCaseApiById(String id);
}
