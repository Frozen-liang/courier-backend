package com.sms.satp.repository.impl;

import com.sms.satp.common.field.CommonFiled;
import com.sms.satp.entity.datacollection.DataCollection;
import com.sms.satp.repository.CommonDeleteRepository;
import com.sms.satp.repository.CustomizedDataCollectionRepository;
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
    private final CommonDeleteRepository commonDeleteRepository;
    private static final String PARAM_LIST = "paramList";

    public CustomizedDataCollectionRepositoryImpl(MongoTemplate mongoTemplate,
        CommonDeleteRepository commonDeleteRepository) {
        this.mongoTemplate = mongoTemplate;
        this.commonDeleteRepository = commonDeleteRepository;
    }


    @Override
    public List<String> getParamListById(String id) {
        Query query = new Query(Criteria.where(CommonFiled.ID.getFiled()).is(id));
        query.fields().include(PARAM_LIST);
        DataCollection dataCollection = mongoTemplate.findOne(query, DataCollection.class);
        if (Objects.nonNull(dataCollection)) {
            return dataCollection.getParamList();
        }
        return Collections.emptyList();
    }

    @Override
    public Boolean deleteById(String id) {
        return commonDeleteRepository.deleteById(id, DataCollection.class);
    }

    @Override
    public Boolean deleteByIds(List<String> ids) {
        return commonDeleteRepository.deleteByIds(ids, DataCollection.class);
    }
}