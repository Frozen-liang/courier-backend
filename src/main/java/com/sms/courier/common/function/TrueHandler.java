package com.sms.courier.common.function;

import io.vavr.Function0;

@FunctionalInterface
public interface TrueHandler {

    void handler(Function0 confirmed);
}
