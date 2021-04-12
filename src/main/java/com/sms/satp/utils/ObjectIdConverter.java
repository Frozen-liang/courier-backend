package com.sms.satp.utils;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

public class ObjectIdConverter {

    public static ObjectId toObjectId(String objectIdStr) {
        return StringUtils.isNotEmpty(objectIdStr) ? new ObjectId(objectIdStr) : null;
    }
}
