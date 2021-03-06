package com.sms.courier.repository;

import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CaseTemplateApiRepository extends MongoRepository<CaseTemplateApiEntity, String> {

    Long deleteAllByIdIsIn(List<String> ids);

    List<CaseTemplateApiEntity> findAllByCaseTemplateIdAndRemovedOrderByOrder(String caseTemplateId, boolean isRemoved);

    List<CaseTemplateApiEntity> findAllByIdIsIn(List<String> ids);

    Long deleteAllByCaseTemplateIdIsIn(List<String> ids);
}
