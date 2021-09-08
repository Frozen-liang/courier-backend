package com.sms.courier.repository;

import com.sms.courier.entity.scenetest.CaseTemplateEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CaseTemplateRepository extends MongoRepository<CaseTemplateEntity, String> {

    Long deleteAllByIdIsIn(List<String> ids);
}
