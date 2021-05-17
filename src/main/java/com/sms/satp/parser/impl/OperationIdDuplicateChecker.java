package com.sms.satp.parser.impl;

import com.sms.satp.common.enums.ImportStatus;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.project.ProjectImportFlowEntity;
import com.sms.satp.parser.ApiDocumentChecker;
import com.sms.satp.repository.ProjectImportFlowRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationContext;

public class OperationIdDuplicateChecker implements ApiDocumentChecker {

    @Override
    public boolean check(List<ApiEntity> waitApiEntities, ProjectImportFlowEntity projectImportFlowEntity,
        ApplicationContext context) {
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
            projectImportFlowEntity.setImportStatus(ImportStatus.FAILED);
            projectImportFlowEntity.setEndTime(LocalDateTime.now());
            projectImportFlowEntity.setErrorDetail(builder.toString());
            ProjectImportFlowRepository projectImportFlowRepository =
                context.getBean(ProjectImportFlowRepository.class);
            projectImportFlowRepository.save(projectImportFlowEntity);
            return false;

        }
        return true;

    }
}
