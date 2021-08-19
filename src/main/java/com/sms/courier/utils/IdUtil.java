package com.sms.courier.utils;

import com.sms.courier.infrastructure.id.DefaultIdentifierGenerator;
import com.sms.courier.infrastructure.id.IdentifierGenerator;

public class IdUtil {

    private static final IdentifierGenerator IDENTIFIER_GENERATOR = DefaultIdentifierGenerator.getSharedInstance();

    public static Long generatorId() {
        return IDENTIFIER_GENERATOR.nextId();
    }
}
