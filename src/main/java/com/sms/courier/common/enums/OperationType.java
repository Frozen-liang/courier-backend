package com.sms.courier.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;

public enum OperationType implements EnumCommon {

    // 增加
    ADD(0),
    // 修改
    EDIT(1),
    // 逻辑删除或真实删除
    DELETE(2),
    // 同步Api
    SYNC(3),
    // 回收站删除
    REMOVE(4),
    // 清空回收站
    CLEAR_RECYCLE_BIN(5),
    // 恢复
    RECOVER(6),
    // 用户加锁
    LOCK(7),
    // 用户解锁
    UNLOCK(9),
    // 登录
    LOGIN(10),
    // 登出
    LOGOUT(11),
    // 重置
    RESET(12),
    //重启
    RESTART(13),
    //用例同步Api
    CASE_SYNC(14),
    //审核
    REVIEW(15);

    private static final Map<Integer, OperationType> MAPPINGS =
        Arrays.stream(values()).sequential().collect(Collectors.toMap(OperationType::getCode, Function.identity()));
    private final int code;

    OperationType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static OperationType getType(@Nullable Integer code) {
        return Objects.isNull(code) ? null : MAPPINGS.get(code);
    }
}
