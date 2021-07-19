package com.sms.satp.repository;

import com.sms.satp.entity.group.CaseTemplateGroupEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CaseTemplateGroupRepository extends MongoRepository<CaseTemplateGroupEntity, String> {

}
