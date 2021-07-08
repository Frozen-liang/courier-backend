package com.sms.satp.repository;

import com.sms.satp.entity.scenetest.CaseTemplateApi;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CaseTemplateApiRepository extends MongoRepository<CaseTemplateApi, String> {

    Long deleteAllByIdIsIn(List<String> ids);

    List<CaseTemplateApi> findAllByIdIsIn(List<String> ids);
}
