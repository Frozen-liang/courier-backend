package com.sms.courier.repository.impl;

import com.sms.courier.common.field.CommonField;
import com.sms.courier.entity.datacollection.DataCollectionEntity;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.CustomizedDataCollectionRepository;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
        return commonRepository.deleteByIds(ids, DataCollectionEntity.class);
    }
}
