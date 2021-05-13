package com.sms.satp.service;

import com.sms.satp.dto.request.BatchAddSceneCaseApiRequest;
import com.sms.satp.dto.request.BatchUpdateSceneCaseApiRequest;
import com.sms.satp.dto.request.UpdateSceneCaseApiRequest;
import com.sms.satp.dto.response.SceneCaseApiResponse;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import java.util.List;

public interface SceneCaseApiService {

    Boolean batchAdd(BatchAddSceneCaseApiRequest addSceneCaseApiDto);

    Boolean deleteByIds(List<String> ids);

    Boolean edit(UpdateSceneCaseApiRequest updateSceneCaseApiRequest);

    Boolean editAll(List<SceneCaseApi> sceneCaseApiList);

    Boolean batchEdit(BatchUpdateSceneCaseApiRequest updateSceneCaseApiDto);

    List<SceneCaseApiResponse> listBySceneCaseId(String sceneCaseId, boolean remove);

    List<SceneCaseApi> listBySceneCaseId(String sceneCaseId);

    SceneCaseApiResponse getSceneCaseApiById(String id);
}
