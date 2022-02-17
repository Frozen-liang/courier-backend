package com.sms.courier.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum ReviewStatus implements EnumCommon {

    PENDING_REVIEW(0),
    UNDER_REVIEW(1),
    REVIEW_NO_PASS(2),
    REVIEW_PASSED(3);

    private final int code;

    @Override
    public int getCode() {
        return this.code;
    }

    ReviewStatus(int code) {
        this.code = code;
    }

    private static final Map<Integer, ReviewStatus> mappings = new HashMap<>(16);

    static {
        for (ReviewStatus reviewStatus : values()) {
            mappings.put(reviewStatus.getCode(), reviewStatus);
        }
    }

    public static ReviewStatus getType(Integer code) {
        return mappings.get(code);
    }

}
