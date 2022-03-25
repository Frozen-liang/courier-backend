package com.sms.courier.service;

import com.sms.courier.common.enums.ApiBindingStatus;
import com.sms.courier.dto.request.BatchAddSceneCaseApiRequest;
import com.sms.courier.dto.request.SyncApiRequest;
import com.sms.courier.dto.request.UpdateSceneCaseApiRequest;
import com.sms.courier.dto.response.SceneCaseApiResponse;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import java.util.List;

public interface SceneCaseApiService {

    Boolean batchAdd(BatchAddSceneCaseApiRequest addSceneCaseApiDto);

    Boolean deleteByIds(List<String> ids);

    Boolean edit(UpdateSceneCaseApiRequest updateSceneCaseApiRequest);

    List<SceneCaseApiEntity> listBySceneCaseId(String sceneCaseId);

    SceneCaseApiResponse getSceneCaseApiById(String id);

    SceneCaseApiEntity findById(String id);

    Boolean updateStatusByApiIds(List<String> ids, ApiBindingStatus apiBindingStatus);

    Long deleteAllBySceneCaseIds(List<String> ids);

    boolean existsByCaseTemplateId(List<String> caseTemplateIds);

    Boolean syncApi(SyncApiRequest request);
}
