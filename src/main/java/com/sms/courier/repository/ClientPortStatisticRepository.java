package com.sms.courier.repository;

import com.sms.courier.entity.statistics.ClientPortStatisticEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientPortStatisticRepository extends MongoRepository<ClientPortStatisticEntity, String> {

}
