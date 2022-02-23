package com.sms.courier.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.courier.dto.request.WebhookRequest;
import com.sms.courier.dto.response.WebhookTypeResponse;
import com.sms.courier.webhook.enums.WebhookType;
import com.sms.courier.webhook.model.WebhookEntity;
import com.sms.courier.webhook.model.WebhookTypeEntity;
import java.util.List;
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

    @Test
    @DisplayName("Test the method to convert the WebhookType entity object to a dto object")
    void toWebhookType_test() {
        WebhookTypeEntity webhookTypeEntity = new WebhookTypeEntity();
        webhookTypeEntity.setDefaultPayload("type");
        webhookTypeEntity.setType(WebhookType.SCHEDULE_END);
        List<WebhookTypeResponse> webhookTypeResponses = webhookMapper.toWebhookTypeList(List.of(webhookTypeEntity));
        assertThat(webhookTypeResponses).isNotEmpty();
    }

}
