package com.sms.satp.repository;

import com.sms.satp.engine.enums.EngineStatus;
import com.sms.satp.engine.model.EngineMemberEntity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EngineMemberRepository extends MongoRepository<EngineMemberEntity, String> {

    Optional<EngineMemberEntity> findFirstBySessionId(String sessionId);

    Optional<EngineMemberEntity> findFirstByDestination(String destination);

    Stream<EngineMemberEntity> findAllByStatus(EngineStatus status);

    List<EngineMemberEntity> findAllByDestinationIn(List<String> destinations);

}
