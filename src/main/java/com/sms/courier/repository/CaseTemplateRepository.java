package com.sms.courier.repository;

import com.sms.courier.entity.scenetest.CaseTemplateEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CaseTemplateRepository extends MongoRepository<CaseTemplateEntity, String> {

}
