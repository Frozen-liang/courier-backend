package com.sms.courier.repository;

import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import java.util.List;

public interface CustomizedSceneCaseApiRepository {

    List<SceneCaseApiEntity> findSceneCaseApiByApiIds(List<String> ids);

    Boolean deleteSceneCaseApiConn(List<String> caseTemplateApiId);

    List<SceneCaseApiEntity> findSceneCaseApiIdsBySceneCaseIds(List<String> ids);

    Boolean deleteByIds(List<String> sceneCaseApiIds);

    Boolean recover(List<String> sceneCaseApiIds);
}
