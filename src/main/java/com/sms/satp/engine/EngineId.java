package com.sms.satp.engine;

import org.bson.types.ObjectId;

public abstract class EngineId {

    public static final String KEY_TEMPLATE = "/engine/%s/invoke";

    public static String generate() {
        return String.format(KEY_TEMPLATE, ObjectId.get());
    }
}
