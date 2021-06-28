package com.sms.satp.repository;

import com.sms.satp.entity.tag.ApiTag;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiTagRepository extends MongoRepository<ApiTag, String> {

    Long deleteAllByIdIsIn(List<String> ids);

    Stream<ApiTag> findAllByIdIn(List<String> ids);

}
