package com.sms.courier.repository;

import com.sms.courier.entity.notification.NotificationTemplateEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationTemplateRepository extends MongoRepository<NotificationTemplateEntity, String>  {

    NotificationTemplateEntity findByType(Integer type);
}
