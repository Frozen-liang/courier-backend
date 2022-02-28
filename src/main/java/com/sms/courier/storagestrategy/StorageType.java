package com.sms.courier.storagestrategy;

import com.sms.courier.common.enums.EnumCommon;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
public enum StorageType implements EnumCommon {
    AMAZON(0),
    MONGO(1);

    private static final Map<Integer, StorageType> MAPPINGS = Arrays.stream(values()).sequential().collect(
            Collectors.toMap(StorageType::getCode, Function.identity()));
    private final int code;

    StorageType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Nullable
    public static StorageType getType(@Nullable Integer code) {
        return (code != null ? MAPPINGS.get(code) : null);
    }
}
