package com.sms.courier.service;

import com.sms.courier.dto.request.AddCaseTemplateConnRequest;
import com.sms.courier.dto.request.AddSceneCaseApiByIdsRequest;
import com.sms.courier.dto.request.AddSceneCaseRequest;
import com.sms.courier.dto.request.CopyStepsRequest;
import com.sms.courier.dto.request.SearchSceneCaseRequest;
import com.sms.courier.dto.request.UpdateSceneCaseConnRequest;
import com.sms.courier.dto.request.UpdateSceneCaseRequest;
import com.sms.courier.dto.response.SceneCaseApiConnResponse;
import com.sms.courier.dto.response.SceneCaseResponse;
import com.sms.courier.dto.response.SceneTemplateResponse;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

public interface SceneCaseService {

    Boolean add(AddSceneCaseRequest addSceneCaseRequest);

    Boolean deleteByIds(List<String> ids);

    Boolean edit(UpdateSceneCaseRequest updateSceneCaseRequest);

    Page<SceneCaseResponse> page(SearchSceneCaseRequest searchDto, ObjectId projectId);

    SceneTemplateResponse getConn(String id);

    Boolean editConn(UpdateSceneCaseConnRequest updateSceneTemplateRequest);

    List<SceneCaseEntity> get(String groupId, String projectId);

    Boolean addApi(AddSceneCaseApiByIdsRequest request);

    Boolean addTemplate(AddCaseTemplateConnRequest addCaseTemplateConnRequest);

    Boolean delete(List<String> ids);

    Boolean recover(List<String> ids);

    List<SceneCaseResponse> getByApiId(String apiId);

    List<SceneCaseApiConnResponse> removeRef(String id, String caseTemplateId, int order);

    Boolean copySteps(CopyStepsRequest request);
}
