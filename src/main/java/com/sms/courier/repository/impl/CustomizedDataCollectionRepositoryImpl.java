package com.sms.courier.repository.impl;

import static com.sms.courier.common.field.SceneField.ENV_DATA_COLL_CONN_LIST;
import static com.sms.courier.common.field.SceneField.SCENE_CASE_DATA_COLL_ID;

import com.sms.courier.common.field.CommonField;
import com.sms.courier.entity.datacollection.DataCollectionEntity;
import com.sms.courier.entity.scenetest.CaseTemplateEntity;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.CustomizedDataCollectionRepository;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedDataCollectionRepositoryImpl implements CustomizedDataCollectionRepository {

    private final MongoTemplate mongoTemplate;
    private final CommonRepository commonRepository;
    private static final String PARAM_LIST = "paramList";

    public CustomizedDataCollectionRepositoryImpl(MongoTemplate mongoTemplate,
        CommonRepository commonRepository) {
        this.mongoTemplate = mongoTemplate;
        this.commonRepository = commonRepository;
    }


    @Override
    public List<String> getParamListById(String id) {
        Query query = new Query(Criteria.where(CommonField.ID.getName()).is(id));
        query.fields().include(PARAM_LIST);
        DataCollectionEntity dataCollection = mongoTemplate.findOne(query, DataCollectionEntity.class);
        return Objects.nonNull(dataCollection) ? dataCollection.getParamList() : Collections.emptyList();
    }

    @Override
    public Boolean deleteById(String id) {
        return commonRepository.deleteById(id, DataCollectionEntity.class);
    }

    @Override
    public Boolean deleteByIds(List<String> ids) {
        for (String id : ids) {
            Document document = new Document();
            document.put("dataCollId", id);
            Update update = new Update();
            update.pull(ENV_DATA_COLL_CONN_LIST.getName(), document);
            Query query = new Query();
            query.addCriteria(Criteria.where(SCENE_CASE_DATA_COLL_ID.getName()).is(new ObjectId(id)));
            mongoTemplate.updateMulti(query, update, SceneCaseEntity.class);
            mongoTemplate.updateMulti(query, update, CaseTemplateEntity.class);
        }
        return commonRepository.deleteByIds(ids, DataCollectionEntity.class);
    }
}
