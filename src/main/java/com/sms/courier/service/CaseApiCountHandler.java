package com.sms.courier.service;

import java.util.List;

public interface CaseApiCountHandler {

    void addTemplateCaseByCaseTemplateApiIds(List<String> ids);

    void deleteTemplateCaseByCaseTemplateApiIds(List<String> ids);

    void deleteSceneCaseBySceneCaseApiIds(List<String> ids);

    void addSceneCaseBySceneCaseApiIds(List<String> sceneCaseApiIds);

    void addSceneCaseByApiIds(List<String> apiIds, boolean isNowObject);

    void addTestCaseByApiIds(List<String> apiIds, Integer count);

    void deleteTestCaseByApiIds(List<String> apiIds);
}
