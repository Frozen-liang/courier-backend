package com.sms.courier.repository;

import com.sms.courier.entity.structure.StructureEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DataStructureRepository extends MongoRepository<StructureEntity, String> {

    Boolean deleteByIdIn(List<String> ids);

    boolean existsByRefStructIds(String id);
}
