package com.sms.courier.common.function;

public class FunctionHandler {

    public static BranchHandler isTrueOrFalse(boolean value) {
        return (confirmed, negate) -> {
            if (value) {
                confirmed.run();
            } else {
                negate.run();
            }
        };
    }

    public static TrueHandler isTrue(boolean value) {
        return (confirmed -> {
            if (value) {
                confirmed.run();
            }
        });
    }

}
