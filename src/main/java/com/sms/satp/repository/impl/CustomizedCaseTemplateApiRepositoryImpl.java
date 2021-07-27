package com.sms.satp.repository.impl;

import com.sms.satp.common.field.CommonField;
import com.sms.satp.common.field.SceneField;
import com.sms.satp.entity.scenetest.CaseTemplateApiEntity;
import com.sms.satp.repository.CommonRepository;
import com.sms.satp.repository.CustomizedCaseTemplateApiRepository;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class CustomizedCaseTemplateApiRepositoryImpl implements CustomizedCaseTemplateApiRepository {

    private final MongoTemplate mongoTemplate;
    private final CommonRepository commonRepository;

    public CustomizedCaseTemplateApiRepositoryImpl(MongoTemplate mongoTemplate,
        CommonRepository commonRepository) {
        this.mongoTemplate = mongoTemplate;
        this.commonRepository = commonRepository;
    }

    @Override
    public List<CaseTemplateApiEntity> findByCaseTemplateIdAndIsExecuteAndIsRemove(String caseTemplateId,
        boolean isExecute, boolean isRemove) {
        Query query = new Query();
        SceneField.CASE_TEMPLATE_ID.is(caseTemplateId).ifPresent(query::addCriteria);
        SceneField.API_IS_EXECUTE.is(isExecute).ifPresent(query::addCriteria);
        CommonField.REMOVE.is(isRemove).ifPresent(query::addCriteria);
        return mongoTemplate.find(query, CaseTemplateApiEntity.class);
    }

    @Override
    public List<CaseTemplateApiEntity> findCaseTemplateApiIdsByCaseTemplateIds(List<String> ids) {
        Query query = new Query();
        query.fields().include(CommonField.ID.getName());
        SceneField.CASE_TEMPLATE_ID.in(ids).ifPresent(query::addCriteria);
        return mongoTemplate.find(query, CaseTemplateApiEntity.class);
    }

    @Override
    public Boolean deleteByIds(List<String> caseTemplateApiIds) {
        return commonRepository.deleteByIds(caseTemplateApiIds, CaseTemplateApiEntity.class);
    }

    @Override
    public Boolean recover(List<String> caseTemplateApiIds) {
        return commonRepository.recover(caseTemplateApiIds, CaseTemplateApiEntity.class);
    }

}
