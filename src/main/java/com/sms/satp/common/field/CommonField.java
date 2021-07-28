package com.sms.satp.common.field;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

public enum CommonField implements Field {

    ID("_id"),
    PROJECT_ID("projectId"),
    API_ID("apiId"),
    CREATE_DATE_TIME("createDateTime"),
    MODIFY_DATE_TIME("modifyDateTime"),
    REMOVE("isRemoved"),
    CREATE_USER_ID("createUserId"),
    GROUP_ID("groupId");

    private final String name;

    CommonField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Optional<Criteria> projectIdIs(String projectId) {
        return StringUtils.isEmpty(projectId) ? Optional.of(Criteria.where(getName()).exists(false)) :
            Optional.of(Criteria.where(getName()).is(projectId));
    }
}
