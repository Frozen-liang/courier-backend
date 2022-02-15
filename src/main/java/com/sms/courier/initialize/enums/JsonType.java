package com.sms.courier.initialize.enums;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sms.courier.docker.entity.ContainerSettingEntity;
import com.sms.courier.engine.model.EngineSettingEntity;
import com.sms.courier.entity.generator.GeneratorTemplateEntity;
import com.sms.courier.entity.generator.GeneratorTemplateTypeEntity;
import com.sms.courier.entity.system.SystemRoleEntity;
import com.sms.courier.entity.system.UserGroupEntity;
import com.sms.courier.webhook.model.WebhookTypeEntity;
import java.util.List;

public enum JsonType {

    SYSTEM_ROLE_ENTITY(new TypeReference<List<SystemRoleEntity>>() {
    }),
    USER_GROUP_ENTITY(new TypeReference<List<UserGroupEntity>>() {
    }),
    ENGINE_SETTING_ENTITY(new TypeReference<List<EngineSettingEntity>>() {
    }),
    CONTAINER_SETTING_ENTITY(new TypeReference<List<ContainerSettingEntity>>() {
    }),
    WEBHOOK_TYPE_ENTITY(new TypeReference<List<WebhookTypeEntity>>() {
    }),
    GENERATOR_TEMPLATE_ENTITY(new TypeReference<List<GeneratorTemplateEntity>>() {
    }),
    GENERATOR_TEMPLATE_TYPE_ENTITY(new TypeReference<List<GeneratorTemplateTypeEntity>>() {
    });

    private final TypeReference<?> typeReference;

    JsonType(TypeReference<?> typeReference) {
        this.typeReference = typeReference;
    }

    public TypeReference<?> getTypeReference() {
        return typeReference;
    }

}
