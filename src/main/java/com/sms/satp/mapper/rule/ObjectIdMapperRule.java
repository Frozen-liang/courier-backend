package com.sms.satp.mapper.rule;

import java.util.Objects;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
public class ObjectIdMapperRule {

    public String asId(ObjectId objectId) {
        return Objects.isNull(objectId) ? "" : objectId.toString();
    }

    public ObjectId asId(String id) {
        return new ObjectId(id);
    }
}
