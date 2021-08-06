package com.sms.courier.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;

public enum OperationModule implements EnumCommon {
    PROJECT(0, "Project"),
    API(1, "Api"),
    API_TEST_CASE(2, "ApiTestCase"),
    SCENE_CASE(3, "SceneCase"),
    SCENE_CASE_API(4, "SceneCaseApi"),
    CASE_TEMPLATE_API(5, "CaseTemplateApi"),
    CASE_TEMPLATE(6, "CaseTemplate"),
    GLOBAL_ENV(7, "GlobalEnvironment"),
    PROJECT_ENV(8, "ProjectEnvironment"),
    GLOBAL_FUNCTION(9, "GlobalFunction"),
    PROJECT_FUNCTION(10, "ProjectFunction"),
    DATA_COLLECTION(11, "DataCollection"),
    API_TAG(12, "ApiTag"),
    API_TAG_GROUP(13, "ApiTagGroup"),
    API_GROUP(14, "ApiGroup"),
    SCENE_CASE_GROUP(15, "SceneCaseGroup"),
    CASE_TEMPLATE_GROUP(16, "CaseTemplateGroup"),
    TEST_FILE(17, "TestFile"),
    WORKSPACE(18, "Workspace"),
    USER(19, "User"),
    USER_GROUP(20, "UserGroup"),
    SCHEDULE(21, "UserGroup");

    private static final Map<Integer, OperationModule> MAPPINGS =
        Arrays.stream(values()).sequential().collect(Collectors.toMap(OperationModule::getCode, Function.identity()));
    private final int code;
    private final String collectionName;

    OperationModule(int code, String collectionName) {
        this.code = code;
        this.collectionName = collectionName;

    }

    public String getCollectionName() {
        return collectionName;
    }

    @Override
    public int getCode() {
        return code;
    }

    public static OperationModule getType(@Nullable Integer code) {
        return Objects.isNull(code) ? null : MAPPINGS.get(code);
    }
}
