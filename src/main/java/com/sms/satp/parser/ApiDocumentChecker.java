package com.sms.satp.parser;

import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.project.ProjectImportFlowEntity;
import java.util.List;
import org.springframework.context.ApplicationContext;

public interface ApiDocumentChecker {

    boolean check(List<ApiEntity> waitApiEntities, ProjectImportFlowEntity projectImportFlowEntity,
        ApplicationContext context);
}