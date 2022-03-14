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

    static <T> TrueHandler<T> confirmed(boolean value, T param) {
        return (confirmed -> {
            if (value) {
                confirmed.accept(param);
            }
        });
    }

}
