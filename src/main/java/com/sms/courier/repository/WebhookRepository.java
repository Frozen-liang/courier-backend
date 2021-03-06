package com.sms.courier.repository;

import com.sms.courier.webhook.model.WebhookEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WebhookRepository extends MongoRepository<WebhookEntity, String> {

    List<WebhookEntity> findByWebhookType(Integer webHookType);

    void deleteByIdIn(List<String> ids);
}