package com.sms.courier.common.field;

public enum WebhookField implements Field {

    URL("url"),
    WEBHOOK_TYPE("webhookType");

    private final String name;

    WebhookField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
