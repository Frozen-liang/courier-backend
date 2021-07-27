package com.sms.satp.repository.impl;

import com.sms.satp.common.field.CommonFiled;
import com.sms.satp.common.field.SceneFiled;
import com.sms.satp.entity.scenetest.CaseTemplateApiEntity;
import com.sms.satp.repository.CommonDeleteRepository;
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
    private final CommonDeleteRepository commonDeleteRepository;

    public CustomizedCaseTemplateApiRepositoryImpl(MongoTemplate mongoTemplate,
        CommonDeleteRepository commonDeleteRepository) {
        this.mongoTemplate = mongoTemplate;
        this.commonDeleteRepository = commonDeleteRepository;
    }

    @Override
    public List<CaseTemplateApiEntity> findByCaseTemplateIdAndIsExecuteAndIsRemove(String caseTemplateId,
        boolean isExecute, boolean isRemove) {
        Query query = new Query();
        SceneFiled.CASE_TEMPLATE_ID.is(caseTemplateId).ifPresent(query::addCriteria);
        SceneFiled.API_IS_EXECUTE.is(isExecute).ifPresent(query::addCriteria);
        CommonFiled.REMOVE.is(isRemove).ifPresent(query::addCriteria);
        return mongoTemplate.find(query, CaseTemplateApiEntity.class);
    }

    @Override
    public List<CaseTemplateApiEntity> findCaseTemplateApiIdsByCaseTemplateIds(List<String> ids) {
        Query query = new Query();
        query.fields().include(CommonFiled.ID.getFiled());
        SceneFiled.CASE_TEMPLATE_ID.in(ids).ifPresent(query::addCriteria);
        return mongoTemplate.find(query, CaseTemplateApiEntity.class);
    }

    @Override
    public Boolean deleteByIds(List<String> caseTemplateApiIds) {
        return commonDeleteRepository.deleteByIds(caseTemplateApiIds, CaseTemplateApiEntity.class);
    }

    @Override
    public Boolean recover(List<String> caseTemplateApiIds) {
        return commonDeleteRepository.recover(caseTemplateApiIds, CaseTemplateApiEntity.class);
    }

}
