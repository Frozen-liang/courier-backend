package com.sms.satp.repository.impl;

import static com.sms.satp.common.field.ApiTag.GROUP_NAME;
import static com.sms.satp.common.field.ApiTag.TAG_NAME;
import static com.sms.satp.common.field.CommonField.API_ID;
import static com.sms.satp.common.field.CommonField.CREATE_USER_ID;
import static com.sms.satp.common.field.CommonField.ID;
import static com.sms.satp.common.field.CommonField.MODIFY_DATE_TIME;
import static com.sms.satp.common.field.CommonField.PROJECT_ID;
import static com.sms.satp.common.field.CommonField.REMOVE;
import static com.sms.satp.common.field.CommonField.USERNAME;
import static com.sms.satp.common.field.SceneField.CREATE_USER_NAME;
import static com.sms.satp.common.field.SceneField.GROUP_ID;
import static com.sms.satp.common.field.SceneField.NAME;
import static com.sms.satp.common.field.SceneField.PRIORITY;
import static com.sms.satp.common.field.SceneField.TAG_ID;
import static com.sms.satp.common.field.SceneField.TEST_STATUS;

import com.google.common.collect.Lists;
import com.sms.satp.common.enums.ApiBindingStatus;
import com.sms.satp.common.enums.OperationModule;
import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.request.CaseTemplateSearchRequest;
import com.sms.satp.dto.response.ApiTestCaseResponse;
import com.sms.satp.dto.response.CaseTemplateResponse;
import com.sms.satp.entity.apitestcase.ApiTestCaseEntity;
import com.sms.satp.entity.mongo.LookupField;
import com.sms.satp.entity.mongo.LookupVo;
import com.sms.satp.entity.mongo.QueryVo;
import com.sms.satp.repository.CommonRepository;
import com.sms.satp.repository.CustomizedApiTestCaseRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedApiTestCaseRepositoryImpl implements CustomizedApiTestCaseRepository {

    private final MongoTemplate mongoTemplate;
    private final CommonRepository commonRepository;
    private static final String STATUS = "status";

    public CustomizedApiTestCaseRepositoryImpl(MongoTemplate mongoTemplate,
        CommonRepository commonRepository) {
        this.mongoTemplate = mongoTemplate;
        this.commonRepository = commonRepository;
    }


    @Override
    public void updateApiTestCaseStatusByApiId(List<String> apiIds, ApiBindingStatus status) {
        Query query = new Query();
        API_ID.in(apiIds).ifPresent(query::addCriteria);
        Update update = new Update();
        update.set(STATUS, status.getCode());
        update.set(MODIFY_DATE_TIME.getName(), LocalDateTime.now());
        mongoTemplate.updateMulti(query, update, ApiTestCaseEntity.class);
    }

    @Override
    public Boolean deleteById(String id) {
        return commonRepository.deleteById(id, ApiTestCaseEntity.class);
    }

    @Override
    public Boolean deleteByIds(List<String> ids) {
        return commonRepository.deleteByIds(ids, ApiTestCaseEntity.class);
    }

    @Override
    public Boolean recover(List<String> ids) {
        return commonRepository.recover(ids, ApiTestCaseEntity.class);
    }

    @Override
    public List<ApiTestCaseResponse> listByJoin(String apiId, String projectId, boolean removed) {
        List<LookupVo> lookupVoList = getLookupVoList();
        QueryVo queryVo = QueryVo.builder()
            .collectionName("ApiTestCase")
            .lookupVo(lookupVoList)
            .criteriaList(buildCriteria(apiId, projectId, removed))
            .build();
        return commonRepository.list(queryVo, ApiTestCaseResponse.class);
    }

    private List<LookupVo> getLookupVoList() {
        return Lists.newArrayList(
            LookupVo.builder()
                .from(OperationModule.API_TAG)
                .localField(TAG_ID)
                .foreignField(ID)
                .as("apiTag")
                .queryFields(Lists.newArrayList(LookupField.builder().field(TAG_NAME).build()))
                .build(),
            LookupVo.builder()
                .from(OperationModule.USER)
                .localField(CREATE_USER_ID)
                .foreignField(ID)
                .as("user")
                .queryFields(Lists.newArrayList(LookupField.builder().field(USERNAME).alias("createUsername").build()))
                .build()
        );
    }

    private List<Optional<Criteria>> buildCriteria(String apiId, String projectId, boolean removed) {
        return Lists.newArrayList(
            PROJECT_ID.is(projectId),
            REMOVE.is(removed),
            API_ID.is(apiId)
        );
    }

}
