package com.sms.satp.repository;

import com.sms.satp.entity.scenetest.CaseTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CaseTemplateRepository extends MongoRepository<CaseTemplate, String> {

}
