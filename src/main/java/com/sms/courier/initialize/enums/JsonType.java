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

    SYSTEM_ROLE_ENTITY(SystemRoleEntity.class, new TypeReference<List<SystemRoleEntity>>() {
    }),
    USER_GROUP_ENTITY(UserGroupEntity.class, new TypeReference<List<UserGroupEntity>>() {
    }),
    ENGINE_SETTING_ENTITY(EngineSettingEntity.class, new TypeReference<List<EngineSettingEntity>>() {
    }),
    CONTAINER_SETTING_ENTITY(ContainerSettingEntity.class, new TypeReference<List<ContainerSettingEntity>>() {
    }),
    WEBHOOK_TYPE_ENTITY(WebhookTypeEntity.class, new TypeReference<List<WebhookTypeEntity>>() {
    }),
    GENERATOR_TEMPLATE_ENTITY(GeneratorTemplateEntity.class, new TypeReference<List<GeneratorTemplateEntity>>() {
    }),
    GENERATOR_TEMPLATE_TYPE_ENTITY(GeneratorTemplateTypeEntity.class,
        new TypeReference<List<GeneratorTemplateTypeEntity>>() {
        });

    private final Class<?> entityClass;
    private final TypeReference<?> typeReference;

    JsonType(Class<?> entityClass, TypeReference<?> typeReference) {
        this.entityClass = entityClass;
        this.typeReference = typeReference;
    }

    public TypeReference<?> getTypeReference() {
        return typeReference;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }
}
