package com.sms.courier.repository;

import com.sms.courier.dto.response.UserInfoResponse;
import com.sms.courier.entity.system.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity, String> {

    Optional<UserEntity> findByUsernameOrEmail(String username, String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<UserInfoResponse> findByIdIn(List<String> ids);
}
