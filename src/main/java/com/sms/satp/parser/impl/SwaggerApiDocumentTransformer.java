package com.sms.satp.parser.impl;

import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.project.ProjectEntity;
import com.sms.satp.parser.ApiDocumentTransformer;
import io.swagger.v3.oas.models.OpenAPI;
import java.util.List;

public class SwaggerApiDocumentTransformer implements ApiDocumentTransformer<OpenAPI> {

    @Override
    public List<ApiEntity> toApiEntities(OpenAPI sourceDocument) {

        return null;
    }

    @Override
    public ProjectEntity toProjectEntity(OpenAPI sourceDocument) {
        return null;
    }
}
