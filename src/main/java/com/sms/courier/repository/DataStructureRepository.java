package com.sms.courier.repository;

import com.sms.courier.dto.response.DataStructureResponse;
import com.sms.courier.entity.structure.StructureEntity;
import java.util.List;
import java.util.Set;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DataStructureRepository extends MongoRepository<StructureEntity, String> {

    Boolean deleteByIdIs(String id);

    List<DataStructureResponse> findByRefIdInAndStructTypeAndIdNotIn(List<String> refIds, Integer structType,
        Set<String> ids);

    List<DataStructureResponse> findByRefIdInAndStructType(List<String> refIds, Integer structType);
}
