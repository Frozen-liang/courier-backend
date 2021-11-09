package com.sms.courier.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.courier.dto.request.WebhookRequest;
import com.sms.courier.webhook.model.WebhookEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for WebhookMapper")
class WebhookMapperTest {

    private final WebhookMapper webhookMapper = new WebhookMapperImpl();
    private static final String URL = "webhook";

    @Test
    @DisplayName("Test the method to convert the Webhook's dto object to a entity object")
    void dto_to_entity() {
        WebhookRequest webhookRequest = WebhookRequest.builder()
            .url(URL)
            .build();
        WebhookEntity webhook = webhookMapper.toEntity(webhookRequest);
        assertThat(webhook.getUrl()).isEqualTo(URL);
    }

}
