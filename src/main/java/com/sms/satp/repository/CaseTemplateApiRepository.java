package com.sms.satp.repository;

import com.sms.satp.entity.scenetest.CaseTemplateApiEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CaseTemplateApiRepository extends MongoRepository<CaseTemplateApiEntity, String> {

    Long deleteAllByIdIsIn(List<String> ids);

    List<CaseTemplateApiEntity> findAllByIdIsIn(List<String> ids);

    List<CaseTemplateApiEntity> findAllByCaseTemplateIdOrderByOrder(String caseTemplateId);
}
