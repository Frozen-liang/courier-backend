package com.sms.courier.webhook;

import com.sms.courier.webhook.enums.WebhookType;
import java.util.Date;
import lombok.Data;

@Data
public class WebhookEvent<T> implements Comparable<WebhookEvent<?>> {

    private WebhookType webhookType;
    private T data;
    private Long timestamp;

    private WebhookEvent(WebhookType webhookType, T data, Long timestamp) {
        this.webhookType = webhookType;
        this.data = data;
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(WebhookEvent<?> o) {

        return this.getTimestamp().compareTo(o.getTimestamp());
    }

    public static <R> WebhookEvent<R> create(WebhookType webhookType, R data) {
        return new WebhookEvent<>(webhookType, data, new Date().getTime());
    }
}
