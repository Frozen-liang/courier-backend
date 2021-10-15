package com.sms.courier.common.field;

import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;

public enum ApiCommentField implements Field {

    API_ID("apiId"),
    PARENT_ID("parentId");

    private final String name;

    ApiCommentField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Optional<Criteria> hasParentId(ObjectId value) {
        if (!this.equals(PARENT_ID)) {
            return Optional.empty();
        }
        return Optional.of(Criteria.where(getName()).is(value));
    }
}
