package com.sms.satp.service;

import com.sms.satp.dto.request.AddCaseTemplateConnRequest;
import com.sms.satp.dto.request.AddSceneCaseApiByIdsRequest;
import com.sms.satp.dto.request.AddSceneCaseRequest;
import com.sms.satp.dto.request.SearchSceneCaseRequest;
import com.sms.satp.dto.request.UpdateSceneCaseConnRequest;
import com.sms.satp.dto.request.UpdateSceneCaseRequest;
import com.sms.satp.dto.response.SceneCaseResponse;
import com.sms.satp.dto.response.SceneTemplateResponse;
import com.sms.satp.entity.scenetest.SceneCaseEntity;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

public interface SceneCaseService {

    Boolean add(AddSceneCaseRequest addSceneCaseRequest);

    Boolean deleteByIds(List<String> ids);

    Boolean edit(UpdateSceneCaseRequest updateSceneCaseRequest);

    Boolean batchEdit(List<SceneCaseEntity> sceneCaseList);

    Page<SceneCaseResponse> page(SearchSceneCaseRequest searchDto, ObjectId projectId);

    SceneTemplateResponse getConn(String id);

    Boolean editConn(UpdateSceneCaseConnRequest updateSceneTemplateRequest);

    List<SceneCaseEntity> get(String groupId, String projectId);

    Boolean addApi(AddSceneCaseApiByIdsRequest request);

    Boolean addTemplate(AddCaseTemplateConnRequest addCaseTemplateConnRequest);

    Boolean deleteConn(String sceneCaseApiId);
}
