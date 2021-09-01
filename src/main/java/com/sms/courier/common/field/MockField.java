package com.sms.courier.common.field;

public enum MockField implements Field {

    API_ID("apiId"),
    IS_ENABLE("isEnable"),
    RESPONSE_PARAMS("responseParams"),
    API_ENCODING_TYPE("apiEncodingType");

    private final String name;

    MockField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
