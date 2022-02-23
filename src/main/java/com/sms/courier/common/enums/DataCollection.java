package com.sms.courier.common.enums;

public enum DataCollection {
    COLLECTION_NAME("collectionName");

    private final String name;

    DataCollection(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
