package com.sms.courier.initialize.enums;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sms.courier.entity.system.SystemRoleEntity;
import com.sms.courier.entity.system.UserGroupEntity;
import java.util.List;

public enum JsonType {

    SYSTEM_ROLE_ENTITY(SystemRoleEntity.class, new TypeReference<List<SystemRoleEntity>>() {
    }),
    USER_GROUP_ENTITY(UserGroupEntity.class, new TypeReference<List<UserGroupEntity>>() {
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
