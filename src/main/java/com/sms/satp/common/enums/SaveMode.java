package com.sms.satp.common.enums;

import com.sms.satp.parser.DiffApiEntitiesFactory;
import com.sms.satp.parser.impl.CoverApiEntitiesFactory;
import com.sms.satp.parser.impl.IncrementApiEntitiesFactory;
import com.sms.satp.parser.impl.RemainApiEntitiesFactory;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;

public enum SaveMode implements EnumCommon {
    COVER(1, new CoverApiEntitiesFactory()),
    REMAIN(2, new RemainApiEntitiesFactory()),
    INCREMENT(3, new IncrementApiEntitiesFactory());

    private static final Map<Integer, SaveMode> MAPPINGS = Arrays.stream(values()).sequential().collect(
        Collectors.toMap(SaveMode::getCode, Function.identity()));


    private final int code;
    private final DiffApiEntitiesFactory diffApiEntitiesFactory;

    SaveMode(int code, DiffApiEntitiesFactory diffApiEntitiesFactory) {
        this.code = code;
        this.diffApiEntitiesFactory = diffApiEntitiesFactory;
    }

    @Override
    public int getCode() {
        return code;
    }

    public DiffApiEntitiesFactory getBuildDiffApiEntities() {
        return diffApiEntitiesFactory;
    }

    @Nullable
    public static SaveMode getType(@Nullable Integer code) {
        return (code != null ? MAPPINGS.get(code) : null);
    }

}
