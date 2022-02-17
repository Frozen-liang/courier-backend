package com.sms.courier.common.field;

public enum SceneField implements Field {

    NAME("name"),
    GROUP_ID("groupId"),
    TEST_STATUS("testStatus"),
    TAG_ID("tagId"),
    PRIORITY("priority"),
    CREATE_USER_NAME("createUserName"),
    CASE_TEMPLATE_ID("caseTemplateId"),
    SCENE_CASE_ID("sceneCaseId"),
    STATUS("status"),
    PARENT_ID("parentId"),
    ORDER("order"),
    API_ID("apiTestCase.apiEntity._id"),
    API_IS_EXECUTE("apiTestCase.isExecute"),
    CASE_TEMPLATE_API_CONN_LIST("caseTemplateApiConnList"),
    IS_LOCK("isLock"),
    API_ID_GROUP_SEARCH("apiTestCase.apiEntity.id"),
    JOB_ENV_ID("environment.id"),
    API_TYPE("apiType"),
    API_TEST_CASE_PROJECT_ID("apiTestCase.projectId"),
    ENV_DATA_COLL_CONN_LIST("envDataCollConnList"),
    SCENE_CASE_DATA_COLL_ID("envDataCollConnList.dataCollId"),
    IS_SQL_RESULT("isSqlResult"),
    REVIEW_STATUS("reviewStatus");

    private final String name;

    SceneField(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
