package com.sms.courier.common.function;

public class FunctionHandle {

    public static BranchHandle isTrueOrFalse(boolean value) {
        return (trueHandle, falseHandle) -> {
            if (value) {
                trueHandle.run();
            } else {
                falseHandle.run();
            }
        };
    }

    public static TrueHandle isTrue(boolean value) {
        return (trueHandle -> {
            if (value) {
                trueHandle.run();
            }
        });
    }

}
