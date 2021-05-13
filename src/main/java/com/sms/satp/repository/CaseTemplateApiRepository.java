package com.sms.satp.repository;

import com.sms.satp.entity.scenetest.CaseTemplateApi;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CaseTemplateApiRepository extends MongoRepository<CaseTemplateApi,String> {

}
