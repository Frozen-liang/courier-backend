package com.sms.courier.common.exception;

public enum ErrorCode {

    SYSTEM_ERROR("9999", "System error"),
    EMAIL_CONFIGURATION_ALREADY_EXIST("10001", "The email configuration has already exist"),
    EMAIL_CONFIGURATION_NOT_EXIST("10002", "The email configuration is not exist"),
    MAIL_SERVICE_IS_DISABLE("10003", "Mail service is disabled"),
    UPDATE_NOTIFICATION_TEMPLATE_ERROR("10004", "Failed to update notification template"),
    NOTIFICATION_TEMPLATE_NOT_EXIST("10005", "The %s is not exist"),
    GET_NOTIFICATION_TEMPLATE_ERROR("10006", "Failed to get the notification template"),
    UPDATE_EMAIL_CONFIGURATION_ERROR("10007", "Failed to update the email configuration!"),
    ENABLE_EMAIL_SERVICE_ERROR("10008", "Failed to enable the email service!"),
    DISABLE_EMAIL_SERVICE_ERROR("10009", "Failed to disable the email service!"),
    GET_EMAIL_CONFIGURATION_ERROR("10010", "Failed to get the email configuration!"),
    RESET_ALREADY_PROCESSING("10100", "The reset is already being processed."),
    USER_RESET_CODE_WRONG("10101", "The reset code was wrong."),
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
    UNLOCK_USER_BY_ID_ERROR("4169", "Failed to unlock the User!"),
    RECOVER_SCENE_CASE_ERROR("4170", "Failed to recover the SceneCase!"),
    RECOVER_CASE_TEMPLATE_ERROR("4171", "Failed to recover the CaseTemplate!"),
    ADD_API_GROUP_ERROR("4172", "Failed to add the ApiGroup!"),
    DELETE_API_GROUP_BY_ID_ERROR("4173", "Failed to delete the ApiGroup!"),
    EDIT_API_GROUP_ERROR("4174", "Failed to edit the ApiGroup!"),
    THE_FUNCTION_KEY_EXIST_ERROR("4175", "The function key %s exist in %s"),
    THE_API_ENTITY_NOT_EXITS_ERROR("4176", "The Api entity no exist!"),
    ADD_SCHEDULE_ERROR("4177", "Failed to add the Schedule!"),
    GET_SCHEDULE_LIST_ERROR("4178", "Failed to get the Schedule list!"),
    GET_SCHEDULE_BY_ID_ERROR("4179", "The Schedule not exist!"),
    DELETE_SCHEDULE_BY_ID_ERROR("4180", "Failed to delete the Schedule!"),
    EDIT_SCHEDULE_ERROR("4181", "Failed to edit the Schedule!"),
    BUILD_CASE_JOB_ERROR("4182", "Failed to build the case job!"),
    BATCH_UPDATE_ERROR("4183", "Batch update %s error. ids:%s key:%s value:%s!"),
    ADD_DATA_STRUCTURE_ERROR("4184", "Failed to add the DataStructure!"),
    GET_DATA_STRUCTURE_LIST_ERROR("4185", "Failed to get the DataStructure list!"),
    GET_DATA_STRUCTURE_BY_ID_ERROR("4186", "The DataStructure not exist!"),
    DELETE_DATA_STRUCTURE_BY_ID_ERROR("4187", "Failed to delete the DataStructure!"),
    EDIT_DATA_STRUCTURE_ERROR("4188", "Failed to edit the DataStructure!"),
    THE_NAME_EXISTS_ERROR("4189", "The name of the %s already exists!"),
    GET_DATA_STRUCTURE_DATA_LIST_ERROR("4190", "Failed to get the DataStructure data list!"),
    ADD_MOCK_API_ERROR("4191", "Failed to add the MockApi!"),
    GET_MOCK_API_PAGE_ERROR("4192", "Failed to get the MockApi page!"),
    GET_MOCK_API_LIST_ERROR("4193", "Failed to get the MockApi list!"),
    EDIT_MOCK_API_ERROR("4194", "Failed to edit the MockApi!"),
    DELETE_MOCK_API_ERROR("4195", "Failed to delete the MockApi!"),
    EDIT_MOCK_SETTING_API_ERROR("4196", "Failed to edit the Mock Setting!"),
    QUERY_MOCK_SETTING_API_ERROR("4197", "Failed to query the Mock Setting!"),
    ADD_LOGIN_SETTING_ERROR("4198", "Failed to add the LoginSetting!"),
    GET_LOGIN_SETTING_LIST_ERROR("4199", "Failed to get the LoginSetting list!"),
    GET_LOGIN_SETTING_BY_ID_ERROR("4200", "The LoginSetting not exist!"),
    DELETE_LOGIN_SETTING_BY_ID_ERROR("4201", "Failed to delete the LoginSetting!"),
    EDIT_LOGIN_SETTING_ERROR("4202", "Failed to edit the LoginSetting!"),
    CIRCULAR_REFERENCE_ERROR("4203", "There are Circular references."),
    GET_DATA_STRUCTURE_REF_LIST_ERROR("4204", "Failed to get the DataStructure ref list!"),
    DATE_STRUCTURE_CANNOT_DELETE_ERROR("4205", "Delete fail. This data structure has %s references!"),
    ACCOUNT_NOT_EXIST("4206", "The user account does not exist"),
    GET_SCENE_CASE_COUNT_ERROR("4207", "Failed to get the SceneCase count!"),
    GET_SCENE_COUNT_BY_API_ERROR("4208", "Failed to query scene count the Api!"),
    ADD_SCHEDULE_GROUP_ERROR("4209", "Failed to add the ScheduleGroup!"),
    GET_SCHEDULE_GROUP_LIST_ERROR("4210", "Failed to get the ScheduleGroup list!"),
    GET_SCHEDULE_GROUP_BY_ID_ERROR("4211", "The ScheduleGroup not exist!"),
    DELETE_SCHEDULE_GROUP_BY_ID_ERROR("4212", "Failed to delete the ScheduleGroup!"),
    EDIT_SCHEDULE_GROUP_ERROR("4213", "Failed to edit the ScheduleGroup!"),
    GET_WORKSPACE_CASE_COUNT_ERROR("4214", "Failed to get the Workspace case count!"),
    GET_WORKSPACE_CASE_ERROR("4215", "Failed to get the Workspace case!"),
    GET_SCHEDULE_RECORD_PAGE_ERROR("4216", "Failed to get the ScheduleRecord page!"),
    CASE_TYPE_ERROR("4217", "The caseType not exists!"),
    ADD_API_COMMENT_ERROR("4218", "Failed to add the ApiComment!"),
    GET_API_COMMENT_LIST_ERROR("4219", "Failed to get the ApiComment list!"),
    DELETE_API_COMMENT_BY_ID_ERROR("4220", "Failed to delete the ApiComment!"),
    THE_REPLIED_COMMENT_NOT_EXIST("4221", "The replied comment does not exist!"),
    GET_CASE_COUNT_BY_API_ERROR("4222", "Failed to query case count the Api!"),
    GET_ENGINE_SETTING_ERROR("4223", "Failed to get engine setting!"),
    EDIT_ENGINE_SETTING_ERROR("4224", "Failed to edit engine setting!"),
    PULL_IMAGE_ERROR("4225", "Pull image: %s error!"),
    DELETE_CONTAINER_ERROR("4226", "Failed to delete service!"),
    RESTART_CONTAINER_ERROR("4227", "Failed to restart service!"),
    NO_SUCH_CONTAINER_ERROR("4228", "No such container: %s"),
    NO_SUCH_IMAGE_ERROR("4229", "No such image: %s"),
    THE_CONTAINER_ALREADY_EXISTED_ERROR("4230", "Service: %s starting, please try again later!"),
    QUERY_CONTAINER_LOG_ERROR("4231", "Failed to query service log!"),
    CREATE_ENGINE_ERROR("4232", "Failed to create engine!"),
    DELETE_ENGINE_ERROR("4233", "Failed to delete engine!"),
    GET_ENGINE_BY_ID_ERROR("4234", "The engine not exist!"),
    RESTART_ENGINE_ERROR("4235", "Failed to restart engine!"),
    GET_CONTAINER_SETTING_ERROR("4236", "The container setting not exist, please configure it!"),
    RESET_API_VERSION_ERROR("4237", "Failed to reset api version!"),
    GET_MOCK_SETTING_BY_ID_ERROR("4238", "Failed to get the Mock setting by id!"),
    RESET_MOCK_SETTING_TOKEN_ERROR("4239", "Failed to reset the Mock Setting token!"),
    CREATE_MOCK_ERROR("4240", "Failed to create mock!"),
    RESTART_MOCK_ERROR("4241", "Failed to restart mock!"),
    DELETE_MOCK_ERROR("4242", "Failed to delete mock!"),
    ADD_WEBHOOK_ERROR("4243", "Failed to add the Webhook!"),
    GET_WEBHOOK_PAGE_ERROR("4244", "Failed to get the Webhook page!"),
    GET_WEBHOOK_BY_ID_ERROR("4245", "The Webhook not exist!"),
    DELETE_WEBHOOK_BY_ID_ERROR("4246", "Failed to delete the Webhook!"),
    EDIT_WEBHOOK_ERROR("4247", "Failed to edit the Webhook!"),
    DELETE_SCHEDULE_CASE_ID_ERROR("4248", "Failed to delete the Schedule case id!"),
    GET_WORKSPACE_CASE_GROUP_BY_DAY_ERROR("4249", "Failed to get the Workspace case group by day!"),
    GET_PROJECT_CASE_GROUP_BY_DAY_ERROR("4250", "Failed to get the Project case group by day!"),
    GET_SCENE_COUNT_API_PAGE_ERROR("4251", "Failed to get scene count api page!"),
    GET_CASE_COUNT_API_PAGE_ERROR("4252", "Failed to get case count api page!"),
    GET_PROJECT_SCENE_CASE_GROUP_BY_DAY_ERROR("4253", "Failed to get the Project scene case group by day!"),
    GET_PROJECT_API_COUNT_ERROR("4254", "Failed to get the Project api count!"),
    GET_PROJECT_SCENE_COUNT_ERROR("4255", "Failed to get the Project scene count!"),
    GET_PROJECT_API_CASE_COUNT_ERROR("4256", "Failed to get the Project api case count!"),
    EDIT_COURIER_SCHEDULER_ERROR("4257", "Failed to edit the Courier scheduler!"),
    GET_COURIER_SCHEDULER_ERROR("4258", "The courier scheduler setting not exist, please configure it!"),
    GET_CASE_JOB_COUNT_ERROR("4259", "Failed to get case job count!"),
    GET_SCENE_CASE_JOB_COUNT_ERROR("4260", "Failed to get scene case job count!"),
    UPDATE_CASE_BY_API_ERROR("4261", "Failed to update case by api error!"),
    GET_WORKSPACE_COUNT_ERROR("4264", "Failed to get the Workspace count!"),
    GET_WORKSPACE_SCENE_CASE_GROUP_BY_DAY_ERROR("4265", "Failed to get the Workspace scene case group by day!"),
    GET_WORKSPACE_CASE_JOB_GROUP_BY_DAY_ERROR("4266", "Failed to get the Workspace case job group by day!"),
    GET_WORKSPACE_SCENE_CASE_JOB_GROUP_BY_DAY_ERROR("4267", "Failed to get the Workspace scene case job group by day!"),
    GET_WORKSPACE_CASE_GROUP_BY_USER_ERROR("4268", "Failed to get the Workspace case group by user!"),
    GET_WORKSPACE_JOB_GROUP_BY_USER_ERROR("4270", "Failed to get the Workspace job group by user!"),
    ENV_CANNOT_REPEATED("4272", "The environment cannot be repeated!"),
    GET_DATABASE_BY_ID_ERROR("4273", "Failed to get the Database by id!"),
    ADD_DATABASE_ERROR("4274", "Failed to add the database!"),
    EDIT_DATABASE_ERROR("4275", "Failed to edit the database!"),
    DELETE_DATABASE_BY_IDS_ERROR("4276", "Failed to delete database by ids!"),
    ADD_AUTH_SETTING_ERROR("4274", "Failed to add the AuthSetting!"),
    GET_AUTH_SETTING_BY_ID_ERROR("4275", "The AuthSetting not exist!"),
    DELETE_AUTH_SETTING_BY_ID_ERROR("4276", "Failed to delete the AuthSetting!"),
    EDIT_AUTH_SETTING_ERROR("4277", "Failed to edit the AuthSetting!"),
    GET_AUTH_SETTING_LIST_ERROR("4278", "Failed to get the AuthSetting page!"),
    GET_SCENE_CASE_BY_API_ID_ERROR("4278", "Failed to get the SceneCase by apiId!"),
    GET_WORKSPACE_PROJECT_CASE_PERCENTAGE_ERROR("4279", "Failed to get workspace project case percentage!"),
    ADD_GENERATOR_TEMPLATE_ERROR("4281", "Failed to add the GeneratorTemplate!"),
    DELETE_GENERATOR_TEMPLATE_ERROR("4282", "Failed to delete the GeneratorTemplate!"),
    EDIT_GENERATOR_TEMPLATE_ERROR("4283", "Failed to edit the GeneratorTemplate!"),
    GET_GENERATOR_TEMPLATE_ERROR("4284", "Failed to get the GeneratorTemplate by id!"),
    GET_GENERATOR_TEMPLATE_LIST_ERROR("4285", "Failed to get the GeneratorTemplate list!"),
    ADD_OPEN_API_SETTING_ERROR("4286", "Failed to add the OpenApiSetting!"),
    GET_OPEN_API_SETTING_LIST_ERROR("4287", "Failed to get the OpenApiSetting list!"),
    GET_OPEN_API_SETTING_BY_ID_ERROR("4288", "The OpenApiSetting not exist!"),
    DELETE_OPEN_API_SETTING_BY_ID_ERROR("4289", "Failed to delete the OpenApiSetting!"),
    EDIT_OPEN_API_SETTING_ERROR("4290", "Failed to edit the OpenApiSetting!"),
    SYNC_API_ERROR("4291", "Failed to sync api!"),
    ADD_SCENE_CASE_COMMENT_ERROR("4292", "Failed to add the SceneCaseComment!"),
    GET_SCENE_CASE_COMMENT_LIST_ERROR("4293", "Failed to get the SceneCaseComment list!"),
    DELETE_SCENE_CASE_COMMENT_BY_ID_ERROR("4294", "Failed to delete the SceneCaseComment!"),
    EXPORT_DATA_COLLECTION_ERROR("4295", "Failed to export the DataCollection!"),
    DATA_COLLECTION_NOTEXITS_ERROR("4296", "The dataCollection not exits. id = %s"),
    COPY_SCENE_STEPS_ERROR("4297", "Failed to copy scene steps!"),
    QUERY_AWS3_SETTING_API_ERROR("4297", "Failed to query the AmazonStorageSetting!"),
    EDIT_AWS3_SETTING_ERROR("4298", "Failed to edit the AmazonStorageSetting!"),
    DELETE_AWS3_SETTING_BY_ID_ERROR("4299", "Failed to delete the AmazonStorageSetting!"),
    BUCKET_NOT_EXIST_ERROR("4300", "The %s not found. id=%s"),
    CONFIG_AMAZON_SERVICE_ERROR("4301", "The amazonS3 not config!"),
    STORE_AMAZON_SERVICE_ERROR("4302", "Failed to store the File!"),
    DELETE_AMAZON_SERVICE_ERROR("4303", "Failed to delete the File!"),
    DOWNLOAD_AMAZON_SERVICE_ERROR("4304", "Failed to download the File!"),
    UPDATE_AMAZON_SERVICE_ERROR("4305", "Failed to update the File!"),
    GET_SCHEDULE_RECORD_BY_ID_ERROR("4306", "Failed to get the ScheduleRecord by id!"),
    ADD_CLIENT_PORT_STATISTIC_ERROR("4307", "Failed to add the ClientPortStatistic!"),
    GET_CLIENT_PORT_STATISTIC_COUNT_ERROR("4308", "Failed to get the ClientPortStatistic count!"),

    // 50001 - 51000 describes code generate exception
    FILE_FILL_THE_CONTENT_ERROR("50001", "File fill the content error!"),
    MUSTACHE_RENDERED_ERROR("50002", "Mustache Rendering error!"),
    GENERATE_CODE_ERROR("50003", "Failed to generate code!"),
    API_CAN_NOT_BE_NULL("50004", "Api can not be null!"),
    TEMPLATE_CAN_NOT_BE_NULL("50005", "Template can not be null!"),
    CODE_TYPE_IS_NOT_EXIST("50006", "Code type is not exist!"),
    ENTITY_SERIALIZATION_ERROR("50007", "Entity serialization error!"),
    FILE_COMPRESSION_ERROR("50008", "File compression error!"),


    // 60001 - 61000 describes api import exception
    PARSE_SWAGGER_FILE_ERROR("60001", "Parse the swagger file error, Please check the format of the file contents."),
    PARSE_SWAGGER_URL_ERROR("60002", "Parse the swagger url error, Please check the url."),
    THE_OPERATION_ID_NOT_UNIQUE_ERROR("60003", "%s"),
    CASE_SYNC_API_ERROR("60004", "Sync api fail."),


    // 61001  describes the test exception
    THE_CASE_NOT_EXIST("61001", "The case must not be empty."),
    THE_ENV_NOT_EXIST("61002", "The Env not exist"),
    THE_DATA_IS_NOT_BINDING_THE_ENV("61003", "This dataset is bound to other environments,"
        + " please go to the Dataset Settings page to view");


    private final String code;
    private final String message;

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
