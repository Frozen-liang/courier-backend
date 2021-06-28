package com.sms.satp.repository;

import com.sms.satp.entity.system.UserEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity, String> {

    Optional<UserEntity> findByUsernameOrEmailAndEnabledTrue(String username, String email);
}
