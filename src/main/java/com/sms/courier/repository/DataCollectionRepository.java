package com.sms.courier.repository;

import com.sms.courier.entity.datacollection.DataCollectionEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DataCollectionRepository extends MongoRepository<DataCollectionEntity, String> {

    List<DataCollectionEntity> findAllByEnvIdExistsAndRemovedAndProjectId(boolean exists, boolean removed,
        String projectId);

}