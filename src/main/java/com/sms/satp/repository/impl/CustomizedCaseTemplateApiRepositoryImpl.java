package com.sms.satp.repository.impl;

import com.sms.satp.common.field.SceneFiled;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import com.sms.satp.repository.CustomizedCaseTemplateApiRepository;
import java.util.Collections;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class CustomizedCaseTemplateApiRepositoryImpl implements CustomizedCaseTemplateApiRepository {

    private final MongoTemplate mongoTemplate;

    public CustomizedCaseTemplateApiRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<CaseTemplateApi> findByCaseTemplateIds(List<String> caseTemplateIds) {
        if (caseTemplateIds.isEmpty()) {
            return Collections.emptyList();
        }
        Query query = new Query();
        SceneFiled.CASE_TEMPLATE_ID.in(caseTemplateIds).ifPresent(query::addCriteria);
        return mongoTemplate.find(query, CaseTemplateApi.class);
    }

}
