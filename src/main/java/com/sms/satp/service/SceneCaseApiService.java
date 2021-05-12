package com.sms.satp.service;

import com.sms.satp.dto.BatchAddSceneCaseApiRequest;
import com.sms.satp.dto.SceneCaseApiResponse;
import com.sms.satp.dto.UpdateSceneCaseApiDto;
import com.sms.satp.dto.UpdateSceneCaseApiRequest;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import java.util.List;

public interface SceneCaseApiService {

    Boolean batchAdd(BatchAddSceneCaseApiRequest addSceneCaseApiDto);

    Boolean deleteByIds(List<String> ids);

    Boolean edit(UpdateSceneCaseApiRequest updateSceneCaseApiRequest);

    Boolean editAll(List<SceneCaseApi> sceneCaseApiList);

    Boolean batchEdit(UpdateSceneCaseApiDto updateSceneCaseApiSortOrderDto);

    List<SceneCaseApiResponse> listBySceneCaseId(String sceneCaseId, boolean remove);

    List<SceneCaseApi> listBySceneCaseId(String sceneCaseId);

    SceneCaseApiResponse getSceneCaseApiById(String id);
}
