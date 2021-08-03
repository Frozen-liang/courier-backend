package com.sms.courier.service;

import com.sms.courier.common.enums.ApiBindingStatus;
import com.sms.courier.dto.request.BatchAddSceneCaseApiRequest;
import com.sms.courier.dto.request.BatchUpdateSceneCaseApiRequest;
import com.sms.courier.dto.request.UpdateSceneCaseApiRequest;
import com.sms.courier.dto.response.SceneCaseApiResponse;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import java.util.List;

public interface SceneCaseApiService {

    Boolean batchAdd(BatchAddSceneCaseApiRequest addSceneCaseApiDto);

    Boolean deleteByIds(List<String> ids);

    Boolean edit(UpdateSceneCaseApiRequest updateSceneCaseApiRequest);

    Boolean batchEdit(BatchUpdateSceneCaseApiRequest updateSceneCaseApiDto);

    List<SceneCaseApiResponse> listBySceneCaseId(String sceneCaseId, boolean removed);

    List<SceneCaseApiEntity> listBySceneCaseId(String sceneCaseId);

    List<SceneCaseApiEntity> getApiBySceneCaseId(String sceneCaseId, boolean removed);

    SceneCaseApiResponse getSceneCaseApiById(String id);

    Boolean updateStatusByApiIds(List<String> ids, ApiBindingStatus apiBindingStatus);
}
