package com.sms.satp.repository;

import com.sms.satp.entity.scenetest.CaseTemplateConn;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CaseTemplateConnRepository extends MongoRepository<CaseTemplateConn, String> {

}
