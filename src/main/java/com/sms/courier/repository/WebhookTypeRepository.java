package com.sms.courier.repository;

import com.sms.courier.webhook.model.WebhookTypeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WebhookTypeRepository extends MongoRepository<WebhookTypeEntity, String> {

}