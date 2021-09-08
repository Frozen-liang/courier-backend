package com.sms.courier.repository;

import com.sms.courier.entity.structure.StructureRefRecordEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DataStructureRefRecordRepository extends MongoRepository<StructureRefRecordEntity, String> {

    List<StructureRefRecordEntity> findByIdIn(List<String> ids);
}
