package com.sms.courier.repository;

import com.sms.courier.common.enums.ContainerStatus;
import com.sms.courier.engine.enums.EngineStatus;
import com.sms.courier.engine.model.EngineMemberEntity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EngineMemberRepository extends MongoRepository<EngineMemberEntity, String> {

    Stream<EngineMemberEntity> findAllByStatus(EngineStatus status);

    List<EngineMemberEntity> findAllByContainerStatusInOrderByCreateDateTimeDesc(
        List<ContainerStatus> containerStatuses);

    Stream<EngineMemberEntity> findAllByContainerStatusOrStatus(ContainerStatus status,
        EngineStatus engineStatus);

    List<EngineMemberEntity> findAllByContainerStatus(ContainerStatus status);

    Optional<EngineMemberEntity> findFirstByName(String name);
}
