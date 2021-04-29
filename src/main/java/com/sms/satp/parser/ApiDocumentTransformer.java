package com.sms.satp.parser;

import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.project.ProjectEntity;
import java.util.List;

public interface ApiDocumentTransformer<T> {

    List<ApiEntity> toApiEntities(T sourceDocument, String projectId);

    ProjectEntity toProjectEntity(T sourceDocument);

}
