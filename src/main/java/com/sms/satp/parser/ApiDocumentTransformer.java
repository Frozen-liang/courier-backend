package com.sms.satp.parser;

import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.group.ApiGroupEntity;
import com.sms.satp.parser.common.DocumentDefinition;
import java.util.List;
import java.util.Set;

public interface ApiDocumentTransformer<T> {

    List<ApiEntity> toApiEntities(DocumentDefinition<T> definition);

    Set<ApiGroupEntity> toApiGroupEntities(DocumentDefinition<T> definition);

}
