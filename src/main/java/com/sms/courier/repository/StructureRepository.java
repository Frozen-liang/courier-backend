package com.sms.courier.repository;

import com.sms.courier.entity.structure.StructureEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StructureRepository extends MongoRepository<StructureEntity, String> {

}
