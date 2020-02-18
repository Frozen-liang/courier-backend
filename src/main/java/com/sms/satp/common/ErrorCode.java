package com.sms.satp.common;

public enum ErrorCode {

    SYSTEM_ERROR("9999", "System error"),
    PARSER_OPEN_API_ERROR("30001", "Document of OpenApi parsing has failed"),
    FILE_FORMAT_ERROR("3002","File format error"),
    GET_REF_KEY_ERROR("40001", "The reference value cannot be null"),
    DOCUMENT_TYPE_ERROR("4002", "Document type error"),
    GET_API_INTERFACE_LIST_ERROR("4025","Failed to get the ApiInterface list!"),
    GET_API_INTERFACE_PAGE_ERROR("4003","Failed to get the ApiInterface page!"),
    PARSE_FILE_AND_SAVE_AS_APIINTERFACE_ERROR("4004","Failed to parse the file and save as ApiInterface!"),
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
