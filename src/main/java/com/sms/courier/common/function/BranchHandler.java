package com.sms.courier.common.function;

import io.vavr.Function0;

@FunctionalInterface
public interface BranchHandler {

    void handler(Function0 confirmed, Function0 negate);

}