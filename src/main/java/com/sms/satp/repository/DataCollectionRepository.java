package com.sms.satp.repository;

import com.sms.satp.entity.datacollection.DataCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DataCollectionRepository extends MongoRepository<DataCollection, String> {

}