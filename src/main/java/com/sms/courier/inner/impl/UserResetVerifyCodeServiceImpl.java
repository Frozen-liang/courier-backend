package com.sms.courier.inner.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.inner.UserRestVerifyCodeService;
import java.time.Duration;
import java.util.concurrent.Executors;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.stereotype.Component;

@Component
public class UserResetVerifyCodeServiceImpl implements UserRestVerifyCodeService {

    private final Cache<String, String> codeCache;
    private final Cache<String, String> hotKeyCache;

    public UserResetVerifyCodeServiceImpl() {
        this.codeCache = buildCache(10L, "user-refresh-thread-%d");
        this.hotKeyCache = buildCache(1L, "reset-hot-key-thread-%d");
    }

    @Override
    public boolean verifyCode(String email, String code) {
        String value = codeCache.getIfPresent(email);
        codeCache.invalidate(email);
        return StringUtils.isNoneBlank(value) && code.equalsIgnoreCase(value);
    }

    @Override
    public String generateCode(String email) {
        String code = RandomStringUtils.randomAlphanumeric(6);
        codeCache.put(email, code);
        return code;
    }

    @Override
    public void verifyIfProcessing(String email) {
        String value = hotKeyCache.getIfPresent(email);
        if (StringUtils.isNoneBlank(value)) {
            throw new ApiTestPlatformException(ErrorCode.RESET_ALREADY_PROCESSING);
        } else {
            hotKeyCache.put(email, email);
        }
    }

    @NonNull
    private static Cache<String, String> buildCache(long expiredTime, String threadName) {
        return Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(expiredTime)).maximumSize(10000)
            .executor(Executors
                .newFixedThreadPool(2,
                    new ThreadFactoryBuilder().setNameFormat(threadName).build())).build();
    }
}