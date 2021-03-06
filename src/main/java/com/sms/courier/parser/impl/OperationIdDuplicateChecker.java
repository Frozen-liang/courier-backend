package com.sms.courier.parser.impl;

import static com.sms.courier.common.exception.ErrorCode.THE_OPERATION_ID_NOT_UNIQUE_ERROR;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.parser.ApiDocumentChecker;
import com.sms.courier.utils.ExceptionUtils;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OperationIdDuplicateChecker implements ApiDocumentChecker {

    @Override
    public void check(List<ApiEntity> waitApiEntities) throws ApiTestPlatformException {
        ConcurrentMap<String, List<ApiEntity>> checkResult = waitApiEntities.parallelStream()
            .collect(Collectors.groupingByConcurrent(ApiEntity::getSwaggerId)).entrySet().parallelStream()
            .filter(entry -> entry.getValue().size() > 1)
            .collect(Collectors.toConcurrentMap(Entry::getKey, Entry::getValue));
        if (checkResult.size() > 0) {
            String errorMsg = checkResult.entrySet().stream().map(this::buildErrorMessage)
                .collect(Collectors.joining("\n"));
            throw ExceptionUtils.mpe(THE_OPERATION_ID_NOT_UNIQUE_ERROR, errorMsg);
        }
    }

    private String buildErrorMessage(Entry<String, List<ApiEntity>> entry) {

        String key = entry.getKey();
        String apiPaths = entry.getValue().stream().map(ApiEntity::getApiPath).collect(Collectors.joining(","));
        return String.format("OperationId [%s] is repeated in [%s].", key, apiPaths);

    }


}
