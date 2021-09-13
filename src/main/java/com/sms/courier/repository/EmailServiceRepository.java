package com.sms.courier.repository;

import com.sms.courier.entity.notification.EmailServiceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailServiceRepository extends MongoRepository<EmailServiceEntity, String> {

}
