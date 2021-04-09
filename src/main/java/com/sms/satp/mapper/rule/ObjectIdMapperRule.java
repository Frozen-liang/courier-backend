package com.sms.satp.mapper.rule;

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
public class ObjectIdMapperRule {

    private final static String EMPTY = "";
    private final static int OBJECT_ID_LENGTH = 24;

    public String asId(ObjectId objectId) {
        return Objects.isNull(objectId) ? EMPTY : objectId.toString();
    }

    public ObjectId asId(String id) {
        return StringUtils.isNotEmpty(id) && id.length() == OBJECT_ID_LENGTH ? new ObjectId(id) : null;
    }
}
