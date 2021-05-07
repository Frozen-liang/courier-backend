package com.sms.satp.common;

public enum ErrorCode {

    SYSTEM_ERROR("9999", "System error"),
    PARSER_OPEN_API_ERROR("30001", "Document of OpenApi parsing has failed"),
    FILE_FORMAT_ERROR("30002","File format error"),
    FILE_READ_ERROR("30003","File has been read to fail"),
    GET_REF_KEY_ERROR("40001", "The reference value cannot be null"),
    DOCUMENT_TYPE_ERROR("4002", "Document type error"),
    GET_API_INTERFACE_PAGE_ERROR("4003","Failed to get the ApiInterface page!"),
    PARSE_TO_API_INTERFACE_ERROR("4004","Failed to parse the file or url and save as ApiInterface!"),
    ADD_API_INTERFACE_ERROR("4005","Failed to add the ApiInterface!"),
    GET_API_INTERFACE_BY_ID_ERROR("4006","Failed to get the ApiInterface by id!"),
    DELETE_API_INTERFACE_BY_ID_ERROR("4007","Failed to delete the ApiInterface!"),
    GET_PROJECT_ENVIRONMENT_PAGE_ERROR("4008","Failed to get the ProjectEnvironment page!"),
    ADD_PROJECT_ENVIRONMENT_ERROR("4009","Failed to add the ProjectEnvironment!"),
    EDIT_PROJECT_ENVIRONMENT_ERROR("4010","Failed to edit the ProjectEnvironment!"),
    DELETE_PROJECT_ENVIRONMENT_BY_ID_ERROR("4011","Failed to delete the ProjectEnvironment!"),
    GET_PROJECT_LIST_ERROR("4012","Failed to get the Project list!"),
    GET_PROJECT_PAGE_ERROR("4013","Failed to get the Project page!"),
    ADD_PROJECT_ERROR("4014","Failed to add the Project!"),
    EDIT_PROJECT_ERROR("4015","Failed to edit the Project!"),
    DELETE_PROJECT_BY_ID_ERROR("4016","Failed to delete the Project!"),
    GET_STATUS_CODE_DOC_PAGE_ERROR("4017","Failed to get the StatusCodeDoc page!"),
    ADD_STATUS_CODE_DOC_ERROR("4018","Failed to add the StatusCodeDoc!"),
    EDIT_STATUS_CODE_DOC_ERROR("4019","Failed to edit the StatusCodeDoc!"),
    DELETE_STATUS_CODE_DOC_BY_ID_ERROR("4020","Failed to delete the StatusCodeDoc!"),
    GET_WIKI_PAGE_ERROR("4021","Failed to get the Wiki page!"),
    ADD_WIKI_ERROR("4022","Failed to add the Wiki!"),
    EDIT_WIKI_ERROR("4023","Failed to edit the Wiki!"),
    DELETE_WIKI_BY_ID_ERROR("4024","Failed to delete the Wiki!"),
    GET_API_INTERFACE_LIST_ERROR("4025","Failed to get the ApiInterface list!"),
    EDIT_API_INTERFACE_ERROR("4026","Failed to edit the ApiInterface!"),
    GET_PROJECT_ENVIRONMENT_BY_ID_ERROR("4027","Failed to get the ProjectEnvironment by id!"),
    GET_SCHEMA_PAGE_ERROR("4028","Failed to get the Schema page!"),
    ADD_SCHEMA_ERROR("4029","Failed to add the Schema!"),
    EDIT_SCHEMA_ERROR("4030","Failed to edit the Schema!"),
    GET_SCHEMA_BY_ID_ERROR("4031","Failed to get the Schema by id!"),
    DELETE_SCHEMA_BY_ID_ERROR("4032","Failed to delete the Schema!"),
    GET_PROJECT_BY_ID_ERROR("4033","Failed to get the Project by id!"),
    GET_STATUS_CODE_DOC_BY_ID_ERROR("4034","Failed to get the StatusCodeDoc by id!"),
    GET_WIKI_BY_ID_ERROR("4035","Failed to get the Wiki by id!"),
    GET_INTERFACE_GROUP_LIST_ERROR("4036","Failed to get the Group list!"),
    ADD_INTERFACE_GROUP_ERROR("4037","Failed to add the Group!"),
    EDIT_INTERFACE_GROUP_ERROR("4038","Failed to edit the Group!"),
    DELETE_INTERFACE_GROUP_BY_ID_ERROR("4039","Failed to delete the Group!"),
    IMPORT_FILE_EMPTY_ERROR("4040","Import failed, the file is empty"),
    IMPORT_URL_EMPTY_ERROE("4041", "Import failed, the URL is empty"),
    SAVE_MODE_ERROR("4042","Saving mode error!"),
    GET_TEST_CASE_BY_ID_ERROR("4043","Failed to get the TestCase by id!"),
    ADD_TEST_CASE_ERROR("4044","Failed to add the TestCase!"),
    EDIT_TEST_CASE_ERROR("4045","Failed to edit the TestCase!"),
    DELETE_TEST_CASE_ERROR("4046","Failed to delete the TestCase!"),
    IMPORT_FILE_FORMAT_ERROR("4047","Illegal file"),
    IMPORT_URL_FORMAT_ERROR("4048","Illegal URL"),
    GET_ALL_INTERFACE_TAG_ERROR("4049","Failed to get all tags!"),
    SAVE_INTERFACE_HISTORY_ERROR("4050","Failed to save the interface history!"),
    GET_INTERFACE_HISTORY_BY_ID_ERROR("4051","Failed to get the Interface history by id!"),
    GET_INTERFACE_HISTORY_LIST_ERROR("4052","Failed to get the Interface history list!"),
    ADD_GLOBAL_ENVIRONMENT_ERROR("4053","Failed to add the GlobalEnvironment!"),
    GET_GLOBAL_ENVIRONMENT_BY_ID_ERROR("4054","Failed to get the GlobalEnvironment by id!"),
    EDIT_GLOBAL_ENVIRONMENT_ERROR("4055","Failed to edit the GlobalEnvironment!"),
    GET_GLOBAL_ENVIRONMENT_LIST_ERROR("4056","Failed to get the GlobalEnvironment list!"),
    GET_API_TAG_BY_ID_ERROR("4057","Failed to get the ApiTag by id!"),
    GET_API_TAG_LIST_ERROR("4058","Failed to get the ApiTag list!"),
    ADD_API_TAG_ERROR("4059","Failed to add the ApiTag!"),
    EDIT_API_TAG_ERROR("4060","Failed to edit the ApiTag!"),
    DELETE_API_TAG_BY_ID_ERROR("4061","Failed to delete the ApiTag!"),
    ADD_SCENE_CASE_ERROR("4062","Failed to add the SceneCase!"),
    DELETE_SCENE_CASE_ERROR("4063","Failed to delete the SceneCase!"),
    EDIT_SCENE_CASE_ERROR("4064","Failed to edit the SceneCase!"),
    GET_SCENE_CASE_PAGE_ERROR("4065","Failed to get the SceneCase page!"),
    SEARCH_SCENE_CASE_ERROR("4066","Failed to search the SceneCase!"),
    GET_GLOBAL_FUNCTION_BY_ID_ERROR("4067","Failed to get the ProjectFunction by id!"),
    GET_GLOBAL_FUNCTION_LIST_ERROR("4068","Failed to get the GlobalFunction list!"),
    ADD_GLOBAL_FUNCTION_ERROR("4069","Failed to add the GlobalFunction!"),
    EDIT_GLOBAL_FUNCTION_ERROR("4070","Failed to edit the GlobalFunction!"),
    DELETE_GLOBAL_FUNCTION_BY_ID_ERROR("4071","Failed to delete the GlobalFunction!"),
    GET_PROJECT_FUNCTION_BY_ID_ERROR("4072","Failed to get the ProjectFunction by id!"),
    GET_PROJECT_FUNCTION_LIST_ERROR("4073","Failed to get the ProjectFunction list!"),
    ADD_PROJECT_FUNCTION_ERROR("4074","Failed to add the ProjectFunction!"),
    EDIT_PROJECT_FUNCTION_ERROR("4075","Failed to edit the ProjectFunction!"),
    DELETE_PROJECT_FUNCTION_BY_ID_ERROR("4076","Failed to delete the ProjectFunction!"),
    ADD_SCENE_CASE_API_ERROR("4077", "Failed to add the SceneCaseApi!"),
    DELETE_SCENE_CASE_API_ERROR("4078", "Failed to delete the SceneCaseApi!"),
    EDIT_SCENE_CASE_API_ERROR("4079", "Failed to edit the SceneCaseApi!"),
    BATCH_EDIT_SCENE_CASE_API_ERROR("4080", "Failed to batch edit the SceneCaseApi!"),
    GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR("4081", "Failed to get the SceneCaseApi list by sceneCaseId!"),
    GET_SCENE_CASE_API_BY_ID_ERROR("4082", "Failed to get the SceneCaseApi by id!"),
    GET_PROJECT_ENVIRONMENT_LIST_ERROR("4083","Failed to get the ProjectEnvironment page!"),
    DELETE_GLOBAL_ENVIRONMENT_ERROR_BY_ID("4084","Failed to delete the GlobalEnvironment!"),
    GET_SCENE_CASE_API_LOG_PAGE_ERROR("4085","Failed to get the SceneCaseApiLog page!"),
    ADD_SCENE_CASE_API_LOG_ERROR("4086","Failed to add the SceneCaseApiLog!"),
    GET_DATA_COLLECTION_BY_ID_ERROR("4087", "Failed to get the DataCollection by id!"),
    GET_DATA_COLLECTION_LIST_ERROR("4088", "Failed to get the DataCollection list!"),
    ADD_DATA_COLLECTION_ERROR("4089", "Failed to add the DataCollection!"),
    EDIT_DATA_COLLECTION_ERROR("4090", "Failed to edit the DataCollection!"),
    DELETE_DATA_COLLECTION_BY_ID_ERROR("4091", "Failed to delete the DataCollection!"),
    GET_DATA_COLLECTION_PARAM_LIST_BY_ID_ERROR("4092", "Failed to delete the DataCollection!"),
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
