package com.sms.courier.generator.enums;

public enum CodeEntityName {

    REQUEST_PARAM("RequestDto"),
    QUERY_PARAM("QueryDto"),
    RESPONSE_PARAM("ResponseDto"),
    COURIER_CODEGEN_CONTROLLER("CourierCodegenController"),
    COURIER_CODEGEN_SERVICE("ICourierCodegenService"),
    COURIER_CODEGEN_SERVICE_IMPL("CourierCodegenService");

    private final String name;

    CodeEntityName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
