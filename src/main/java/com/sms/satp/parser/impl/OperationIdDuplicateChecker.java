package com.sms.satp.parser.impl;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.parser.ApiDocumentChecker;
import com.sms.satp.utils.ExceptionUtils;
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
            StringBuilder builder = new StringBuilder();
            for (Entry<String, List<ApiEntity>> entry : checkResult.entrySet()) {
                String key = entry.getKey();
                String apiPaths = entry.getValue().stream().map(ApiEntity::getApiPath).collect(Collectors.joining(","));
                String errorDetail = String.format("OperationId [%s] is repeated in [%s].", key, apiPaths);
                builder.append(errorDetail).append("\n");
            }
            throw ExceptionUtils.mpe(builder.toString());
            /*projectImportFlowEntity.setImportStatus(ImportStatus.FAILED);
            projectImportFlowEntity.setEndTime(LocalDateTime.now());
            projectImportFlowEntity.setErrorDetail(builder.toString());
            ProjectImportFlowRepository projectImportFlowRepository =
                context.getBean(ProjectImportFlowRepository.class);

            log.error("The project whose id is {},{}",
                projectImportFlowEntity.getProjectId(), projectImportFlowEntity.getErrorDetail());
            projectImportFlowRepository.save(projectImportFlowEntity);
            MessageService messageService = context.getBean(MessageService.class);
            messageService.projectMessage(projectImportFlowEntity.getProjectId(),
                Payload.ok(projectImportFlowEntity));*/
        }
    }
}
