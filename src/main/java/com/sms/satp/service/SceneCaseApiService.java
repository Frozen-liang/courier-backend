package com.sms.satp.service;

import com.sms.satp.common.enums.ApiBindingStatus;
import com.sms.satp.dto.request.AddSceneCaseApiByIdsRequest;
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

    List<SceneCaseApiResponse> listBySceneCaseId(String sceneCaseId, boolean removed);

    List<SceneCaseApi> listBySceneCaseId(String sceneCaseId);

    List<SceneCaseApi> getApiBySceneCaseId(String sceneCaseId, boolean removed);

    SceneCaseApiResponse getSceneCaseApiById(String id);

    Boolean add(AddSceneCaseApiByIdsRequest request);

    Boolean updateStatusByApiIds(List<String> ids, ApiBindingStatus apiBindingStatus);
}
