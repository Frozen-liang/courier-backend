package com.sms.courier.repository;

import com.sms.courier.dto.response.UserEnvConnResponse;
import com.sms.courier.entity.env.UserEnvConnEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserEnvRepository extends MongoRepository<UserEnvConnEntity, String> {

    Optional<UserEnvConnResponse> findFirstByProjectIdAndCreateUserId(String project, String currentUserId);

    void deleteByProjectIdAndCreateUserId(String projectId, String currentUserId);
}