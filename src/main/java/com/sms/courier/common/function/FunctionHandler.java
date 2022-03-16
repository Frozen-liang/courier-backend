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

    static <T> TrueHandler<T> confirmed(boolean isConfirmed, T param) {
        return (confirmed -> {
            if (isConfirmed) {
                confirmed.accept(param);
            }
        });
    }

    static <T, R> Function1Handler<T, R> confirmedF1(boolean isConfirmed, T param) {
        return (confirmed -> {
            R value = null;
            if (isConfirmed) {
                value = confirmed.apply(param);
            }
            return value;
        });
    }

}
