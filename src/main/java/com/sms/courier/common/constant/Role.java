package com.sms.courier.common.constant;

import org.springframework.stereotype.Component;

@Component
public class Role {

    private static final String CREATE = "_Create";
    private static final String UPDATE = "_Update";
    private static final String DELETE = "_Delete";
    private static final String CRE_UPD_DEL = "_Create_Update_Delete";
    private static final String QUERY_OWN = "_Query_Own";
    private static final String QUERY_ALL = "_Query_All";

    // Admin
    public static final String ADMIN = "Admin";

    // Workspace
    public static final String WORKSPACE_QUERY_OWN = "Workspace" + QUERY_OWN;

    // User
    public static final String USER_CREATE = "User" + CREATE;
    public static final String USER_UPDATE = "User" + UPDATE;
    public static final String USER_STATUS_CONTROL = "User_Status_Control";
    public static final String USER_QUERY_ALL = "User" + QUERY_ALL;

    // UserGroup
    public static final String USER_GROUP_CRE_UPD_DEL = "User_Group" + CRE_UPD_DEL;
    public static final String USER_GROUP_QUERY_ALL = "User_Group" + QUERY_ALL;

    // Project
    public static final String PROJECT_CREATE = "Project" + CREATE;
    public static final String PROJECT_UPDATE = "Project" + UPDATE;
    public static final String PROJECT_DELETE = "Project" + DELETE;
    public static final String PROJECT_QUERY_ALL = "Project" + QUERY_ALL;
    public static final String PROJECT_QUERY_OWN = "Project" + QUERY_OWN;

    // GlobalEnvironment
    public static final String GLOBAL_ENV_CREATE = "Global_Env" + CREATE;
    public static final String GLOBAL_ENV_UPDATE = "Global_Env" + UPDATE;
    public static final String GLOBAL_ENV_DELETE = "Global_Env" + DELETE;
    public static final String GLOBAL_ENV_QUERY_ALL = "Global_Env" + QUERY_ALL;
    public static final String GLOBAL_ENV_QUERY_OWN = "Global_Env" + QUERY_OWN;

    // GlobalFunction
    public static final String GLOBAL_FUN_CREATE = "Global_Fun" + CREATE;
    public static final String GLOBAL_FUN_UPDATE = "Global_Fun" + UPDATE;
    public static final String GLOBAL_FUN_DELETE = "Global_Fun" + DELETE;
    public static final String GLOBAL_FUN_QUERY_ALL = "Global_Fun" + QUERY_ALL;
    public static final String GLOBAL_FUN_QUERY_OWN = "Global_Fun" + QUERY_OWN;

    // ProjectEnvironment
    public static final String PRO_ENV_CRE_UPD_DEL = "Project_Env" + CRE_UPD_DEL;
    public static final String PRO_ENV_QUERY_ALL = "Project_Env" + QUERY_ALL;
    public static final String PRO_ENV_QUERY_OWN = "Project_Env" + QUERY_OWN;

    // ProjectFunction
    public static final String PRO_FUN_CRE_UPD_DEL = "Project_Fun" + CRE_UPD_DEL;
    public static final String PRO_FUN_QUERY_ALL = "Project_Fun" + QUERY_ALL;
    public static final String PRO_FUN_QUERY_OWN = "Project_Fun" + QUERY_OWN;

    // Api
    public static final String API_CRE_UPD_DEL = "Api" + CRE_UPD_DEL;
    public static final String API_QUERY_ALL = "Api" + QUERY_ALL;
    public static final String API_QUERY_OWN = "Api" + QUERY_OWN;
    public static final String API_SYNC = "Api_Sync";
    public static final String API_IMPORT_BY_FILE = "Api_Import_By_File";

    // ApiGroup
    public static final String API_GROUP_CRE_UPD_DEL = "Api_Group" + CRE_UPD_DEL;
    public static final String API_GROUP_QUERY_ALL = "Api_Group" + QUERY_ALL;
    public static final String API_GROUP_QUERY_OWN = "Api_Group" + QUERY_OWN;

    // ApiTag
    public static final String TAG_CRE_UPD_DEL = "Tag" + CRE_UPD_DEL;
    public static final String TAG_QUERY_ALL = "Tag" + QUERY_ALL;
    public static final String TAG_QUERY_OWN = "Tag" + QUERY_OWN;

    // ApiTagGroup
    public static final String TAG_GROUP_CRE_UPD_DEL = "Tag_Group" + CRE_UPD_DEL;
    public static final String TAG_GROUP_QUERY_ALL = "Tag_Group" + QUERY_ALL;
    public static final String TAG_GROUP_QUERY_OWN = "Tag_Group" + QUERY_OWN;

    // ApiTestCase
    public static final String CASE_CRE_UPD_DEL = "Case" + CRE_UPD_DEL;
    public static final String CASE_QUERY_ALL = "Case" + QUERY_ALL;
    public static final String CASE_QUERY_OWN = "Case" + QUERY_OWN;

    // SceneCase
    public static final String SCENE_CASE_CRE_UPD_DEL = "Scene_Case" + CRE_UPD_DEL;
    public static final String SCENE_CASE_QUERY_ALL = "Scene_Case" + QUERY_ALL;
    public static final String SCENE_CASE_QUERY_OWN = "Scene_Case" + QUERY_OWN;

    // DataCollection
    public static final String DATA_COLLECTION_CRE_UPD_DEL = "Data_Collection" + CRE_UPD_DEL;
    public static final String DATA_COLLECTION_QUERY_ALL = "Data_Collection" + QUERY_ALL;
    public static final String DATA_COLLECTION_QUERY_OWN = "Data_Collection" + QUERY_OWN;

    // TestFile
    public static final String FILE_CRE_UPD_DEL = "Test_File" + CRE_UPD_DEL;
    public static final String FILE_QUERY_ALL = "Test_File" + QUERY_ALL;
    public static final String FILE_QUERY_OWN = "Test_File" + QUERY_OWN;

    //ProjectImportSource
    public static final String PRO_IMP_SOU_CRE_UPD_DEL = "Project_Import_Source" + CRE_UPD_DEL;
    public static final String PRO_IMP_SOU_QUERY_ALL = "Project_Import_Source" + QUERY_ALL;
    public static final String PRO_IMP_SOU_QUERY_OWN = "Project_Import_Source" + QUERY_OWN;

    // CaseTemplateApi
    public static final String CASE_TEMPLATE_API_CRE_UPD_DEL = "Case_Template_Api" + CRE_UPD_DEL;
    public static final String CASE_TEMPLATE_API_QUERY_ALL = "Case_Template_Api" + QUERY_ALL;
    public static final String CASE_TEMPLATE_API_QUERY_OWN = "Case_Template_Api" + QUERY_OWN;

    // CaseTemplate
    public static final String CASE_TEMPLATE_CRE_UPD_DEL = "Case_Template" + CRE_UPD_DEL;
    public static final String CASE_TEMPLATE_QUERY_ALL = "Case_Template" + QUERY_ALL;
    public static final String CASE_TEMPLATE_QUERY_OWN = "Case_Template" + QUERY_OWN;

    // CaseTemplateGroup
    public static final String CASE_TEMPLATE_GROUP_CRE_UPD_DEL = "Case_Template_Group" + CRE_UPD_DEL;
    public static final String CASE_TEMPLATE_GROUP_QUERY_ALL = "Case_Template_Group" + QUERY_ALL;
    public static final String CASE_TEMPLATE_GROUP_QUERY_OWN = "Case_Template_Group" + QUERY_OWN;

    // SceneCaseApi
    public static final String SCENE_CASE_API_CRE_UPD_DEL = "Scene_Case_Api" + CRE_UPD_DEL;
    public static final String SCENE_CASE_API_QUERY_ALL = "Scene_Case_Api" + QUERY_ALL;
    public static final String SCENE_CASE_API_QUERY_OWN = "Scene_Case_Api" + QUERY_OWN;

    // SceneCaseGroup
    public static final String SCENE_CASE_GROUP_CRE_UPD_DEL = "Scene_Case_Group" + CRE_UPD_DEL;
    public static final String SCENE_CASE_GROUP_QUERY_ALL = "Scene_Case_Group" + QUERY_ALL;
    public static final String SCENE_CASE_GROUP_QUERY_OWN = "Scene_Case_Group" + QUERY_OWN;

    //Log
    public static final String LOG_QUERY_ALL = "Log" + QUERY_ALL;
    public static final String LOG_QUERY_OWN = "Log" + QUERY_OWN;

    // Schedule
    public static final String SCHEDULE_CRE_UPD_DEL = "Schedule" + CRE_UPD_DEL;
    public static final String SCHEDULE_QUERY_ALL = "Schedule" + QUERY_ALL;
    public static final String SCHEDULE_QUERY_OWN = "Schedule" + QUERY_OWN;


    // Engine role
    public static final String GLOBAL_FUN_FIND_ALL = "Global_Fun_Find_All";
    public static final String PROJECT_FUN_FIND_ALL = "Project_Fun_Find_All";
    public static final String GLOBAL_FUNCTION_PULL = "Global_Fun_Pull";
    public static final String PROJECT_FUNCTION_PULL = "Project_Fun_Pull";
}
