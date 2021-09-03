package com.sms.courier.common.enums;

import com.sms.courier.dto.response.FileInfoResponse;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.datacollection.DataCollectionEntity;
import com.sms.courier.entity.env.GlobalEnvironmentEntity;
import com.sms.courier.entity.env.ProjectEnvironmentEntity;
import com.sms.courier.entity.function.GlobalFunctionEntity;
import com.sms.courier.entity.function.ProjectFunctionEntity;
import com.sms.courier.entity.group.ApiGroupEntity;
import com.sms.courier.entity.group.ApiTagGroupEntity;
import com.sms.courier.entity.group.CaseTemplateGroupEntity;
import com.sms.courier.entity.group.SceneCaseGroupEntity;
import com.sms.courier.entity.mock.MockApiEntity;
import com.sms.courier.entity.project.ProjectEntity;
import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import com.sms.courier.entity.scenetest.CaseTemplateEntity;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
import com.sms.courier.entity.schedule.ScheduleEntity;
import com.sms.courier.entity.system.UserEntity;
import com.sms.courier.entity.system.UserGroupEntity;
import com.sms.courier.entity.tag.ApiTagEntity;
import com.sms.courier.entity.workspace.WorkspaceEntity;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;

public enum OperationModule implements EnumCommon {
    PROJECT(0, ProjectEntity.class),
    API(1, ApiEntity.class),
    API_TEST_CASE(2, ApiTestCaseEntity.class),
    SCENE_CASE(3, SceneCaseEntity.class),
    SCENE_CASE_API(4, SceneCaseApiEntity.class),
    CASE_TEMPLATE_API(5, CaseTemplateApiEntity.class),
    CASE_TEMPLATE(6, CaseTemplateEntity.class),
    GLOBAL_ENV(7, GlobalEnvironmentEntity.class),
    PROJECT_ENV(8, ProjectEnvironmentEntity.class),
    GLOBAL_FUNCTION(9, GlobalFunctionEntity.class),
    PROJECT_FUNCTION(10, ProjectFunctionEntity.class),
    DATA_COLLECTION(11, DataCollectionEntity.class),
    API_TAG(12, ApiTagEntity.class),
    API_TAG_GROUP(13, ApiTagGroupEntity.class),
    API_GROUP(14, ApiGroupEntity.class),
    SCENE_CASE_GROUP(15, SceneCaseGroupEntity.class),
    CASE_TEMPLATE_GROUP(16, CaseTemplateGroupEntity.class),
    TEST_FILE(17, FileInfoResponse.class),
    WORKSPACE(18, WorkspaceEntity.class),
    USER(19, UserEntity.class),
    USER_GROUP(20, UserGroupEntity.class),
    SCHEDULE(21, ScheduleEntity.class),
    MOCK_API(22, MockApiEntity.class);

    private static final Map<Integer, OperationModule> MAPPINGS =
        Arrays.stream(values()).sequential().collect(Collectors.toMap(OperationModule::getCode, Function.identity()));
    private final int code;
    private final Class<?> entityClass;

    OperationModule(int code, Class<?> entityClass) {
        this.code = code;
        this.entityClass = entityClass;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    @Override
    public int getCode() {
        return code;
    }

    public static OperationModule getType(@Nullable Integer code) {
        return Objects.isNull(code) ? null : MAPPINGS.get(code);
    }
}
