package com.sms.satp.repository;

import com.sms.satp.entity.scenetest.CaseTemplateEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CaseTemplateRepository extends MongoRepository<CaseTemplateEntity, String> {

}
