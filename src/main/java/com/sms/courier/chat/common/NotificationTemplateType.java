package com.sms.courier.chat.common;

import lombok.Getter;

@Getter
public enum NotificationTemplateType {

    ACCOUNT_PWD_RESET(1, "Password-Reset-Template");

    private final int value;
    private final String name;

    NotificationTemplateType(int value, String name) {
        this.value = value;
        this.name = name;
    }
}
