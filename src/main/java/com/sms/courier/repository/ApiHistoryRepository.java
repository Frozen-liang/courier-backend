package com.sms.courier.repository;

import com.sms.courier.entity.api.ApiHistoryEntity;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiHistoryRepository extends MongoRepository<ApiHistoryEntity, String> {

    Stream<ApiHistoryEntity> findAllByIdIn(List<String> ids);
}
