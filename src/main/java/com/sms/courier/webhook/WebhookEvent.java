package com.sms.courier.webhook;

import com.sms.courier.webhook.enums.WebhookType;
import java.util.Date;
import lombok.Data;

@Data
public class WebhookEvent<T> implements Comparable<WebhookEvent<?>> {

    private Integer webhookType;
    private T data;
    private Long timestamp;

    private WebhookEvent(Integer webhookType, T data, Long timestamp) {
        this.webhookType = webhookType;
        this.data = data;
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(WebhookEvent<?> o) {

        return this.getTimestamp().compareTo(o.getTimestamp());
    }

    public static <R> WebhookEvent<R> create(WebhookType webhookType, R data) {
        return new WebhookEvent<>(webhookType.getCode(), data, new Date().getTime());
    }
}
