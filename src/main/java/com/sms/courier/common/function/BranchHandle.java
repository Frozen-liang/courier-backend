package com.sms.courier.common.function;

@FunctionalInterface
public interface BranchHandle {

    void handle(Runnable trueHandle, Runnable falseHandle);

}
