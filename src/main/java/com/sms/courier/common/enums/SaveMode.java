package com.sms.courier.common.enums;

import com.sms.courier.parser.ApiImportHandler;
import com.sms.courier.parser.impl.CoverApiImportHandler;
import com.sms.courier.parser.impl.IncrementApiImportHandler;
import com.sms.courier.parser.impl.RemainApiImportHandler;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;

public enum SaveMode implements EnumCommon {
    COVER(1, new CoverApiImportHandler()),
    REMAIN(2, new RemainApiImportHandler()),
    INCREMENT(3, new IncrementApiImportHandler());

    private static final Map<Integer, SaveMode> MAPPINGS = Arrays.stream(values()).sequential().collect(
        Collectors.toMap(SaveMode::getCode, Function.identity()));


    private final int code;
    private final ApiImportHandler apiImportHandler;

    SaveMode(int code, ApiImportHandler apiImportHandler) {
        this.code = code;
        this.apiImportHandler = apiImportHandler;
    }

    @Override
    public int getCode() {
        return code;
    }

    public ApiImportHandler getApiImportHandler() {
        return apiImportHandler;
    }

    @Nullable
    public static SaveMode getType(@Nullable Integer code) {
        return (code != null ? MAPPINGS.get(code) : null);
    }

}
