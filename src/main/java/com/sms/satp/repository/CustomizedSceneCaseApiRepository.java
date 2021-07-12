package com.sms.satp.repository;

import com.sms.satp.entity.scenetest.SceneCaseApi;
import java.util.List;

public interface CustomizedSceneCaseApiRepository {

    int findCurrentOrderBySceneCaseId(String sceneCaseId);

    List<SceneCaseApi> findSceneCaseApiByApiIds(List<String> ids);

    List<SceneCaseApi> findSceneCaseApiBySceneCaseIdAndIsExecute(String sceneCaseId, Boolean isExecute);

    Boolean deleteSceneCaseApiConn(List<String> caseTemplateApiId);
}
