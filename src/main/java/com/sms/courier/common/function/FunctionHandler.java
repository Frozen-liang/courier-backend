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

    static <T, U> BiConsumerHandler<T, U> confirmedTwoNoReturn(boolean isConfirmed, T paramOne, U paramTwo) {
        return (confirmed -> {
            if (isConfirmed) {
                confirmed.accept(paramOne, paramTwo);
            }
        });
    }

    static <T, R> FunctionOneParamHandler<T, R> confirmedOne(boolean isConfirmed, T param) {
        return (confirmed -> {
            R value = null;
            if (isConfirmed) {
                value = confirmed.apply(param);
            }
            return value;
        });
    }

}
