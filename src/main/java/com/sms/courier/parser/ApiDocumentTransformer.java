package com.sms.courier.parser;

import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.group.ApiGroupEntity;
import com.sms.courier.parser.common.DocumentDefinition;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public interface ApiDocumentTransformer<T> {

    List<ApiEntity> toApiEntities(DocumentDefinition<T> definition, Consumer<ApiEntity> callback);

    Set<ApiGroupEntity> toApiGroupEntities(DocumentDefinition<T> definition, Consumer<ApiGroupEntity> callback);

}
