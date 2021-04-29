package com.sms.satp.parser;

import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.project.ProjectEntity;
import java.util.List;

public interface ApiDocumentTransformer<DocumentParserResult> {

    List<ApiEntity> toApiEntities(DocumentParserResult result, String projectId);

    ProjectEntity toProjectEntity(DocumentParserResult result);

}
