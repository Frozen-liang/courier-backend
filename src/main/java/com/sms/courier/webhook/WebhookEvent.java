package com.sms.courier.webhook;

import com.sms.courier.webhook.enums.WebHookType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebhookEvent<T> implements Comparable<WebhookEvent<?>> {

    private String hookId;
    private String workspaceId;
    private WebHookType webHookType;
    private T data;
    private Long timestamp;

    @Override
    public int compareTo(WebhookEvent<?> o) {

        return this.getTimestamp().compareTo(o.getTimestamp());
    }
}
