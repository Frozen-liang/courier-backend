package com.sms.courier.repository;

import com.sms.courier.dto.response.DataStructureReferenceResponse;
import com.sms.courier.entity.structure.ApiStructureRefRecordEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiDataStructureRefRecordRepository extends MongoRepository<ApiStructureRefRecordEntity, String> {

    List<DataStructureReferenceResponse> findByRefStructIdsIs(String dataStructId);

    boolean existsByRefStructIdsIs(String structId);
}
