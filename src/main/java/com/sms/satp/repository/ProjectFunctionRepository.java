package com.sms.satp.repository;

import com.sms.satp.dto.response.ProjectFunctionResponse;
import com.sms.satp.entity.function.ProjectFunctionEntity;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectFunctionRepository extends MongoRepository<ProjectFunctionEntity, String> {

    Stream<ProjectFunctionResponse> findAllByRemovedIsFalse();

    List<ProjectFunctionResponse> findAllByIdIn(List<String> ids);

}