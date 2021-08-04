package com.sms.courier.repository;

import com.sms.courier.dto.response.GlobalFunctionResponse;
import com.sms.courier.entity.function.GlobalFunctionEntity;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GlobalFunctionRepository extends MongoRepository<GlobalFunctionEntity, String> {

    Stream<GlobalFunctionResponse> findAllByRemovedIsFalse();

    List<GlobalFunctionResponse> findAllByIdIn(List<String> ids);

    boolean existsByFunctionKeyAndWorkspaceIdAndRemovedIsFalse(String functionName, String workspaceId);
}