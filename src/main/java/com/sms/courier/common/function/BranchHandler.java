package com.sms.courier.common.function;

@FunctionalInterface
public interface BranchHandler {

    void handler(Runnable confirmed, Runnable negate);

}
