package com.sms.courier.common.function;

public interface FunctionHandler {

    static BranchHandler confirmedOrNegate(boolean value) {
        return (confirmed, negate) -> {
            if (value) {
                confirmed.apply();
            } else {
                negate.apply();
            }
        };
    }

    static TrueHandler confirmed(boolean value) {
        return (confirmed -> {
            if (value) {
                confirmed.apply();
            }
        });
    }

}
