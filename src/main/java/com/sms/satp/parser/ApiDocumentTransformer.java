package com.sms.satp.parser;

import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.project.ProjectEntity;
import com.sms.satp.parser.common.DocumentParserResult;
import java.util.List;

public interface ApiDocumentTransformer {

    List<ApiEntity> toApiEntities(DocumentParserResult result, String projectId);

    ProjectEntity toProjectEntity(DocumentParserResult result);

}
