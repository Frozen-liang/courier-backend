package com.sms.satp.common.exception;

public enum ErrorCode {

    SYSTEM_ERROR("9999", "System error"),
    PARSER_OPEN_API_ERROR("30001", "Document of OpenApi parsing has failed"),
    FILE_FORMAT_ERROR("30002", "File format error"),
    FILE_READ_ERROR("30003", "File has been read to fail"),
    GET_REF_KEY_ERROR("40001", "The reference value cannot be null"),
    DOCUMENT_TYPE_ERROR("4002", "Document type error"),
    GET_API_PAGE_ERROR("4003", "Failed to get the Api page!"),
    PARSE_TO_API_INTERFACE_ERROR("4004", "Failed to parse the file or url and save as Api!"),
    ADD_API_ERROR("4005", "Failed to add the Api!"),
    GET_API_BY_ID_ERROR("4006", "Failed to get the Api by id!"),
    DELETE_API_BY_ID_ERROR("4007", "Failed to delete the Api!"),
    GET_PROJECT_ENVIRONMENT_PAGE_ERROR("4008", "Failed to get the ProjectEnvironment page!"),
    ADD_PROJECT_ENVIRONMENT_ERROR("4009", "Failed to add the ProjectEnvironment!"),
    EDIT_PROJECT_ENVIRONMENT_ERROR("4010", "Failed to edit the ProjectEnvironment!"),
    DELETE_PROJECT_ENVIRONMENT_BY_ID_ERROR("4011", "Failed to delete the ProjectEnvironment!"),
    GET_PROJECT_LIST_ERROR("4012", "Failed to get the Project list!"),
    GET_PROJECT_PAGE_ERROR("4013", "Failed to get the Project page!"),
    ADD_PROJECT_ERROR("4014", "Failed to add the Project!"),
    EDIT_PROJECT_ERROR("4015", "Failed to edit the Project!"),
    DELETE_PROJECT_BY_ID_ERROR("4016", "Failed to delete the Project!"),
    GET_STATUS_CODE_DOC_PAGE_ERROR("4017", "Failed to get the StatusCodeDoc page!"),
    ADD_STATUS_CODE_DOC_ERROR("4018", "Failed to add the StatusCodeDoc!"),
    EDIT_STATUS_CODE_DOC_ERROR("4019", "Failed to edit the StatusCodeDoc!"),
    DELETE_STATUS_CODE_DOC_BY_ID_ERROR("4020", "Failed to delete the StatusCodeDoc!"),
    GET_WIKI_PAGE_ERROR("4021", "Failed to get the Wiki page!"),
    ADD_WIKI_ERROR("4022", "Failed to add the Wiki!"),
    EDIT_WIKI_ERROR("4023", "Failed to edit the Wiki!"),
    DELETE_WIKI_BY_ID_ERROR("4024", "Failed to delete the Wiki!"),
    GET_API_INTERFACE_LIST_ERROR("4025", "Failed to get the ApiInterface list!"),
    EDIT_API_INTERFACE_ERROR("4026", "Failed to edit the ApiInterface!"),
    GET_PROJECT_ENVIRONMENT_BY_ID_ERROR("4027", "Failed to get the ProjectEnvironment by id!"),
    GET_SCHEMA_PAGE_ERROR("4028", "Failed to get the Schema page!"),
    ADD_SCHEMA_ERROR("4029", "Failed to add the Schema!"),
    EDIT_SCHEMA_ERROR("4030", "Failed to edit the Schema!"),
    GET_SCHEMA_BY_ID_ERROR("4031", "Failed to get the Schema by id!"),
    DELETE_SCHEMA_BY_ID_ERROR("4032", "Failed to delete the Schema!"),
    GET_PROJECT_BY_ID_ERROR("4033", "Failed to get the Project by id!"),
    GET_STATUS_CODE_DOC_BY_ID_ERROR("4034", "Failed to get the StatusCodeDoc by id!"),
    GET_WIKI_BY_ID_ERROR("4035", "Failed to get the Wiki by id!"),
    GET_INTERFACE_GROUP_LIST_ERROR("4036", "Failed to get the Group list!"),
    ADD_INTERFACE_GROUP_ERROR("4037", "Failed to add the Group!"),
    EDIT_INTERFACE_GROUP_ERROR("4038", "Failed to edit the Group!"),
    DELETE_INTERFACE_GROUP_BY_ID_ERROR("4039", "Failed to delete the Group!"),
    IMPORT_FILE_EMPTY_ERROR("4040", "Import failed, the file is empty"),
    IMPORT_URL_EMPTY_ERROE("4041", "Import failed, the URL is empty"),
    SAVE_MODE_ERROR("4042", "Saving mode error!"),
    GET_TEST_CASE_BY_ID_ERROR("4043", "Failed to get the TestCase by id!"),
    ADD_TEST_CASE_ERROR("4044", "Failed to add the TestCase!"),
    EDIT_TEST_CASE_ERROR("4045", "Failed to edit the TestCase!"),
    DELETE_TEST_CASE_ERROR("4046", "Failed to delete the TestCase!"),
    IMPORT_FILE_FORMAT_ERROR("4047", "Illegal file"),
    IMPORT_URL_FORMAT_ERROR("4048", "Illegal URL"),
    GET_ALL_INTERFACE_TAG_ERROR("4049", "Failed to get all tags!"),
    SAVE_INTERFACE_HISTORY_ERROR("4050", "Failed to save the interface history!"),
    GET_INTERFACE_HISTORY_BY_ID_ERROR("4051", "Failed to get the Interface history by id!"),
    GET_INTERFACE_HISTORY_LIST_ERROR("4052", "Failed to get the Interface history list!"),
    ADD_GLOBAL_ENVIRONMENT_ERROR("4053", "Failed to add the GlobalEnvironment!"),
    GET_GLOBAL_ENVIRONMENT_BY_ID_ERROR("4054", "Failed to get the GlobalEnvironment by id!"),
    EDIT_GLOBAL_ENVIRONMENT_ERROR("4055", "Failed to edit the GlobalEnvironment!"),
    GET_GLOBAL_ENVIRONMENT_LIST_ERROR("4056", "Failed to get the GlobalEnvironment list!"),
    GET_API_TAG_BY_ID_ERROR("4057", "Failed to get the ApiTag by id!"),
    GET_API_TAG_LIST_ERROR("4058", "Failed to get the ApiTag list!"),
    ADD_API_TAG_ERROR("4059", "Failed to add the ApiTag!"),
    EDIT_API_TAG_ERROR("4060", "Failed to edit the ApiTag!"),
    DELETE_API_TAG_BY_ID_ERROR("4061", "Failed to delete the ApiTag!"),
    ADD_SCENE_CASE_ERROR("4062", "Failed to add the SceneCase!"),
    DELETE_SCENE_CASE_ERROR("4063", "Failed to delete the SceneCase!"),
    EDIT_SCENE_CASE_ERROR("4064", "Failed to edit the SceneCase!"),
    GET_SCENE_CASE_PAGE_ERROR("4065", "Failed to get the SceneCase page!"),
    SEARCH_SCENE_CASE_ERROR("4066", "Failed to search the SceneCase!"),
    GET_GLOBAL_FUNCTION_BY_ID_ERROR("4067", "Failed to get the ProjectFunction by id!"),
    GET_GLOBAL_FUNCTION_LIST_ERROR("4068", "Failed to get the GlobalFunction list!"),
    ADD_GLOBAL_FUNCTION_ERROR("4069", "Failed to add the GlobalFunction!"),
    EDIT_GLOBAL_FUNCTION_ERROR("4070", "Failed to edit the GlobalFunction!"),
    DELETE_GLOBAL_FUNCTION_BY_ID_ERROR("4071", "Failed to delete the GlobalFunction!"),
    GET_PROJECT_FUNCTION_BY_ID_ERROR("4072", "Failed to get the ProjectFunction by id!"),
    GET_PROJECT_FUNCTION_LIST_ERROR("4073", "Failed to get the ProjectFunction list!"),
    ADD_PROJECT_FUNCTION_ERROR("4074", "Failed to add the ProjectFunction!"),
    EDIT_PROJECT_FUNCTION_ERROR("4075", "Failed to edit the ProjectFunction!"),
    DELETE_PROJECT_FUNCTION_BY_ID_ERROR("4076", "Failed to delete the ProjectFunction!"),
    ADD_SCENE_CASE_API_ERROR("4077", "Failed to add the SceneCaseApi!"),
    DELETE_SCENE_CASE_API_ERROR("4078", "Failed to delete the SceneCaseApi!"),
    EDIT_SCENE_CASE_API_ERROR("4079", "Failed to edit the SceneCaseApi!"),
    BATCH_EDIT_SCENE_CASE_API_ERROR("4080", "Failed to batch edit the SceneCaseApi!"),
    GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR("4081", "Failed to get the SceneCaseApi list by sceneCaseId!"),
    GET_SCENE_CASE_API_BY_ID_ERROR("4082", "Failed to get the SceneCaseApi by id!"),
    GET_PROJECT_ENVIRONMENT_LIST_ERROR("4083", "Failed to get the ProjectEnvironment page!"),
    DELETE_GLOBAL_ENVIRONMENT_ERROR_BY_ID("4084", "Failed to delete the GlobalEnvironment!"),
    GET_DATA_COLLECTION_BY_ID_ERROR("4087", "Failed to get the DataCollection by id!"),
    GET_DATA_COLLECTION_LIST_ERROR("4088", "Failed to get the DataCollection list!"),
    ADD_DATA_COLLECTION_ERROR("4089", "Failed to add the DataCollection!"),
    EDIT_DATA_COLLECTION_ERROR("4090", "Failed to edit the DataCollection!"),
    DELETE_DATA_COLLECTION_BY_ID_ERROR("4091", "Failed to delete the DataCollection!"),
    GET_DATA_COLLECTION_PARAM_LIST_BY_ID_ERROR("4092", "Failed to delete the DataCollection!"),
    EDIT_API_ERROR("4093", "Failed to edit the Api!"),
    ADD_CASE_TEMPLATE_ERROR("4094", "Failed to add the CaseTemplate!"),
    DELETE_CASE_TEMPLATE_ERROR("4095", "Failed to delete the CaseTemplate!"),
    EDIT_CASE_TEMPLATE_ERROR("4096", "Failed to edit the CaseTemplate!"),
    GET_CASE_TEMPLATE_PAGE_ERROR("4097", "Failed to get the CaseTemplate page!"),
    SEARCH_CASE_TEMPLATE_ERROR("4098", "Failed to search the CaseTemplate!"),
    ADD_CASE_TEMPLATE_API_ERROR("4099", "Failed to add the CaseTemplateApi!"),
    DELETE_CASE_TEMPLATE_API_ERROR("4100", "Failed to delete the CaseTemplateApi!"),
    EDIT_CASE_TEMPLATE_API_ERROR("4101", "Failed to edit the CaseTemplateApi!"),
    BATCH_EDIT_CASE_TEMPLATE_API_ERROR("4102", "Failed to batch edit the CaseTemplateApi!"),
    GET_CASE_TEMPLATE_API_LIST_BY_CASE_TEMPLATE_ID_ERROR("4081", "Failed to get the CaseTemplateApi "
        + "list by caseTemplateId!"),
    GET_CASE_TEMPLATE_API_BY_ID_ERROR("4103", "Failed to get the CaseTemplateApi by id!"),
    DELETE_CASE_TEMPLATE_CONN_ERROR("4104", "Failed to delete the CaseTemplateConn!"),
    GET_CASE_TEMPLATE_CONN_LIST_ERROR("4105", "Failed to get the CaseTemplateConn list!"),
    EDIT_CASE_TEMPLATE_CONN_ERROR("4106", "Failed to edit the CaseTemplateConn!"),
    EDIT_LIST_CASE_TEMPLATE_CONN_ERROR("4107", "Failed to editList the CaseTemplateConn!"),
    GET_SCENE_CASE_CONN_ERROR("4108", "Failed to get the SceneCase conn!"),
    EDIT_SCENE_CASE_CONN_ERROR("4109", "Failed to edit the SceneCase conn!"),
    ADD_CASE_TEMPLATE_CONN_ERROR("4110", "Failed to add the CaseTemplateConn!"),
    GET_LOG_PAGE_ERROR("4111", "Failed to get the Log page!"),
    GET_API_TAG_GROUP_BY_ID_ERROR("4112", "Failed to get the ApiTagGroup by id!"),
    GET_API_TAG_GROUP_LIST_ERROR("4113", "Failed to get the ApiTagGroup list!"),
    ADD_API_TAG_GROUP_ERROR("4114", "Failed to add the ApiTagGroup!"),
    EDIT_API_TAG_GROUP_ERROR("4115", "Failed to edit the ApiTagGroup!"),
    DELETE_API_TAG_GROUP_BY_ID_ERROR("4116", "Failed to delete the ApiTagGroup!"),
    THE_API_TAG_GROUP_NAME_EXIST_ERROR("4117", "The %s exist!"),
    EDIT_NOT_EXIST_ERROR("4118", "The %s not found. id=%s"),
    GET_API_TEST_CASE_BY_ID_ERROR("4119", "Failed to get the ApiCase by id!"),
    ADD_API_TEST_CASE_ERROR("4120", "Failed to add the ApiCase!"),
    DELETE_API_TEST_CASE_BY_ID_ERROR("4121", "Failed to delete the ApiCase!"),
    EDIT_API_TEST_CASE_ERROR("4122", "Failed to edit the ApiCase!"),
    GET_API_TEST_CASE_LIST_ERROR("4123", "Failed to get the ApiCase list!"),
    ADD_SCENE_CASE_JOB_ERROR("4124", "Failed to add the SceneCaseJob!"),
    GET_SCENE_CASE_JOB_PAGE_ERROR("4125", "Failed to get the SceneCaseJob page!"),
    GET_SCENE_CASE_JOB_ERROR("4126", "Failed to get the SceneCaseJob!"),
    EDIT_SCENE_CASE_JOB_ERROR("4127", "Failed to edit the SceneCaseJob!"),
    GET_SCENE_CASE_BY_ID_ERROR("4128", "Failed to get the SceneCase by id!"),
    THE_API_TEST_CASE_NOT_EXITS_ERROR("4129", "The ApiTestCase no exist!"),
    THE_ENVIRONMENT_NOT_EXITS_ERROR("4130", "The Environment no exist!"),
    EXECUTE_API_TEST_CASE_ERROR("4131", "Execute the ApiTestCase error!"),
    GET_API_TEST_CASE_JOB_ERROR("4132", "Failed to get the ApiTestCaseJob!"),
    ADD_SCENE_CASE_GROUP_ERROR("4133", "Failed to add the SceneCaseGroup!"),
    EDIT_SCENE_CASE_GROUP_ERROR("4134", "Failed to edit the SceneCaseGroup!"),
    DELETE_SCENE_CASE_GROUP_ERROR("4135", "Failed to delete the SceneCaseGroup!"),
    GET_SCENE_CASE_GROUP_LIST_ERROR("4136", "Failed to get the SceneCaseGroup list!"),
    ADD_CASE_TEMPLATE_GROUP_ERROR("4137", "Failed to add the CaseTemplateGroup!"),
    EDIT_CASE_TEMPLATE_GROUP_ERROR("4138", "Failed to edit the CaseTemplateGroup!"),
    DELETE_CASE_TEMPLATE_GROUP_ERROR("4139", "Failed to delete the CaseTemplateGroup!"),
    GET_CASE_TEMPLATE_GROUP_LIST_ERROR("4140", "Failed to get the CaseTemplateGroup list!"),
    GET_API_GROUP_LIST_ERROR("4141", "Failed to get the ApiGroup list!"),
    THE_PROJECT_EXIST_ERROR("4142", "The project exist! name=%s version=%s"),
    UPLOAD_TEST_FILE_ERROR("4143", "Failed to upload the TestFile!"),
    DELETE_TEST_FILE_BY_ID_ERROR("4144", "Failed to delete the TestFile!"),
    EDIT_TEST_FILE_ERROR("4145", "Failed to edit the TestFile!"),
    GET_TEST_FILE_LIST_ERROR("4146", "Failed to get the TestFile list!"),
    DOWNLOAD_TEST_FILE_ERROR("4147", "Failed to download the TestFile!"),
    THE_TEST_FILE_NOT_EXIST_ERROR("4148", "The TestFile not exist! id=%s"),
    ADD_WORKSPACE_ERROR("4149", "Failed to add the Workspace!"),
    GET_WORKSPACE_LIST_ERROR("4150", "Failed to get the Workspace list!"),
    DELETE_WORKSPACE_BY_ID_ERROR("4151", "Failed to delete the Workspace!"),
    EDIT_WORKSPACE_ERROR("4152", "Failed to edit the Workspace!"),
    GET_WORKSPACE_BY_ID_ERROR("4153", "Failed to get the Workspace by id!"),
    THE_WORKSPACE_CANNOT_DELETE_ERROR("4154", "There are project under this workspace that cannot be deleted!"),
    ADD_USER_ERROR("4155", "Failed to add the User!"),
    GET_USER_LIST_ERROR("4156", "Failed to get the User list!"),
    LOCK_USER_BY_ID_ERROR("4157", "Failed to lock the User!"),
    EDIT_USER_ERROR("4158", "Failed to edit the User!"),
    GET_USER_BY_ID_ERROR("4159", "Failed to get the User by id!"),
    GET_SCENE_CASE_ERROR("4160", "Failed to get the SceneCase!"),
    GET_CASE_TEMPLATE_ERROR("4161", "Failed to get the CaseTemplate!"),
    GET_CASE_TEMPLATE_BY_ID_ERROR("4162", "Failed to get the CaseTemplate by id!"),
    DELETE_SCENE_CASE_CONN_ERROR("4163", "Failed to delete the SceneCase conn!"),
    ADD_USER_GROUP_ERROR("4164", "Failed to add the UserGroup!"),
    GET_USER_GROUP_LIST_ERROR("4165", "Failed to get the UserGroup list!"),
    DELETE_USER_GROUP_BY_ID_ERROR("4166", "Failed to delete the UserGroup!"),
    EDIT_USER_GROUP_ERROR("4167", "Failed to edit the UserGroup!"),
    IMPORT_DATA_COLLECTION_ERROR("4168", "Failed to import the DataCollection!"),
    UNLOCK_USER_BY_ID_ERROR("4157", "Failed to unlock the User!"),
    ADD_API_GROUP_ERROR("4158", "Failed to add the ApiGroup!"),
    DELETE_API_GROUP_BY_ID_ERROR("4159", "Failed to delete the ApiGroup!"),
    EDIT_API_GROUP_ERROR("4160", "Failed to edit the ApiGroup!"),
    // 60001 describes the test exception
    NOT_SUPPORT_METHOD("60001", "Does not support other HTTP methods.");

    private String code;
    private String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
