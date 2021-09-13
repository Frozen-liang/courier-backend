package com.sms.courier.repository;

import com.sms.courier.engine.enums.EngineStatus;
import com.sms.courier.engine.model.EngineMemberEntity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EngineMemberRepository extends MongoRepository<EngineMemberEntity, String> {

    Optional<EngineMemberEntity> findFirstBySessionId(String sessionId);

    Optional<EngineMemberEntity> findFirstByDestination(String destination);

    Stream<EngineMemberEntity> findAllByStatus(EngineStatus status);

    Stream<EngineMemberEntity> findAllByStatusAndOpenIsTrue(EngineStatus status);

    List<EngineMemberEntity> findAllByDestinationIn(List<String> destinations);

}
