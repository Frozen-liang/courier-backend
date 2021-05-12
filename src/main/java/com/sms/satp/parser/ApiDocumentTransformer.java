package com.sms.satp.parser;

import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.parser.common.DocumentDefinition;
import java.util.List;

public interface ApiDocumentTransformer {

    List<ApiEntity> toApiEntities(DocumentDefinition definition);

}
