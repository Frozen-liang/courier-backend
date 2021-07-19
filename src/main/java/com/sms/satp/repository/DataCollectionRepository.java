package com.sms.satp.repository;

import com.sms.satp.entity.datacollection.DataCollectionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DataCollectionRepository extends MongoRepository<DataCollectionEntity, String> {

}