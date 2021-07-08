package com.sms.satp.repository;

import com.sms.satp.entity.scenetest.SceneCaseApi;
import java.util.List;

public interface CustomizedSceneCaseApiRepository {

    SceneCaseApi findMaxOrderBySceneCaseId(String sceneCaseId);

    List<SceneCaseApi> findSceneCaseApiByApiIds(List<String> ids);
}
