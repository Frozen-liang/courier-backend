package com.sms.courier.common.field;

public enum ProjectImportFlowField implements Field {

    IMPORT_SOURCE_ID("importSourceId"),
    IMPORT_STATUS("importStatus");

    private final String name;

    ProjectImportFlowField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
