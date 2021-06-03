package com.sms.satp.common.field;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

public enum CommonFiled implements Filed {

    ID("_id"),
    PROJECT_ID("projectId"),
    API_ID("apiId"),
    CREATE_DATE_TIME("createDateTime"),
    MODIFY_DATE_TIME("modifyDateTime"),
    REMOVE("removed"),
    CREATE_USER_ID("createUserId");

    private final String filed;

    CommonFiled(String filed) {
        this.filed = filed;
    }

    public String getFiled() {
        return filed;
    }

    public Optional<Criteria> projectIdIs(String projectId) {
        return StringUtils.isEmpty(projectId) ? Optional.of(Criteria.where(getFiled()).exists(false)) :
            Optional.of(Criteria.where(getFiled()).is(projectId));
    }
}
