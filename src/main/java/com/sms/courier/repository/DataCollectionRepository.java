package com.sms.courier.repository;

import com.sms.courier.entity.datacollection.DataCollectionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DataCollectionRepository extends MongoRepository<DataCollectionEntity, String> {

}