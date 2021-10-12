package com.sms.courier.repository;

import com.sms.courier.entity.api.ApiCommentEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiCommentRepository extends MongoRepository<ApiCommentEntity, String> {

    void deleteByIdIn(List<String> ids);
}
