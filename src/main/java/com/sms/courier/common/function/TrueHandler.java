package com.sms.courier.common.function;

@FunctionalInterface
public interface TrueHandler {

    void handler(Runnable confirmed);
}
