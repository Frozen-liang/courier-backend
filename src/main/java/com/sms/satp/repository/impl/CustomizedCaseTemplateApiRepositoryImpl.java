package com.sms.satp.repository.impl;

import com.sms.satp.common.field.SceneFiled;
import com.sms.satp.entity.scenetest.CaseTemplateApiEntity;
import com.sms.satp.repository.CustomizedCaseTemplateApiRepository;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
    public List<CaseTemplateApiEntity> findByCaseTemplateIds(List<String> caseTemplateIds) {
        if (caseTemplateIds.isEmpty()) {
            return Collections.emptyList();
        }
        Query query = new Query();
        SceneFiled.CASE_TEMPLATE_ID.in(caseTemplateIds).ifPresent(query::addCriteria);
        return mongoTemplate.find(query, CaseTemplateApiEntity.class);
    }

    @Override
    public List<CaseTemplateApiEntity> findByCaseTemplateIdAndIsExecute(String caseTemplateId, Boolean isExecute) {
        Query query = new Query();
        SceneFiled.CASE_TEMPLATE_ID.is(caseTemplateId).ifPresent(query::addCriteria);
        SceneFiled.API_IS_EXECUTE.is(isExecute).ifPresent(query::addCriteria);
        return mongoTemplate.find(query, CaseTemplateApiEntity.class);
    }

    @Override
    public int findCurrentOrderByCaseTemplateId(String caseTemplateId) {
        Query query = new Query();
        SceneFiled.CASE_TEMPLATE_ID.is(caseTemplateId).ifPresent(query::addCriteria);
        query.with(Sort.by(Direction.DESC, SceneFiled.ORDER.getFiled()));
        query.limit(1);
        CaseTemplateApiEntity caseTemplateApi = mongoTemplate.findOne(query, CaseTemplateApiEntity.class);
        return Objects.isNull(caseTemplateApi) ? 1 : caseTemplateApi.getOrder() + 1;
    }

}
