package com.sms.courier.repository;

import com.sms.courier.entity.tag.ApiTagEntity;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiTagRepository extends MongoRepository<ApiTagEntity, String> {

    Long deleteAllByIdIsIn(List<String> ids);

    Stream<ApiTagEntity> findAllByIdIn(List<String> ids);

}
