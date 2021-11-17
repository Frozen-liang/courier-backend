package com.sms.courier.docker.enmu;

import com.sms.courier.common.field.Field;

public enum ContainerField implements Field {
    CONTAINER_NAME("containerName"),
    CONTAINER_STATUS("containerStatus"),
    NAME("name");

    private final String name;

    ContainerField(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
