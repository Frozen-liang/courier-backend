package com.sms.satp.repository;

import com.mongodb.client.result.UpdateResult;
import com.sms.satp.entity.tag.ApiTag;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiTagRepository extends MongoRepository<ApiTag, String> {

    Long deleteAllByIdIsIn(List<String> ids);

}
