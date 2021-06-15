package com.sms.satp.repository;

import com.sms.satp.entity.scenetest.SceneCaseApi;

public interface CustomizedSceneCaseApiRepository {

    SceneCaseApi findMaxOrderBySceneCaseId(String sceneCaseId);
}
