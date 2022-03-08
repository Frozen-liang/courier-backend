package com.sms.courier.webhook.response;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebhookScheduleResponse {

    private String id;

    private String name;

    private int success;

    private int fail;

    private Map<String, Object> metadata;
}
