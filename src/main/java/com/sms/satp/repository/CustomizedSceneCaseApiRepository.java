package com.sms.satp.repository;

import com.sms.satp.entity.scenetest.SceneCaseApiEntity;
import java.util.List;

public interface CustomizedSceneCaseApiRepository {

    List<SceneCaseApiEntity> findSceneCaseApiByApiIds(List<String> ids);

    List<SceneCaseApiEntity> findSceneCaseApiBySceneCaseIdAndIsRemove(String sceneCaseId, boolean isRemove);

    Boolean deleteSceneCaseApiConn(List<String> caseTemplateApiId);

    List<SceneCaseApiEntity> findSceneCaseApiIdsBySceneCaseIds(List<String> ids);

    Boolean deleteByIds(List<String> sceneCaseApiIds);

    Boolean recover(List<String> sceneCaseApiIds);
}
