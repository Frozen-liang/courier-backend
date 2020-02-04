package com.sms.satp.utils;

import com.google.common.collect.Lists;
import com.sms.satp.entity.Header;
import com.sms.satp.parser.model.ApiHeader;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

public abstract class ApiHeaderConverter {

    private ApiHeaderConverter() {
    }

    public static final Function<List<ApiHeader>, List<Header>> CONVERT_TO_HEADER =
        ApiHeaderConverter::apiHeaderListConvert;

    private static List<Header> apiHeaderListConvert(List<ApiHeader> apiHeaders) {
        return CollectionUtils.isNotEmpty(apiHeaders)
            ? apiHeaders.stream().map(apiHeader -> Header.builder()
            .name(apiHeader.getName())
            .description(apiHeader.getDescription())
            .build())
            .collect(Collectors.toList()) : Lists.newArrayList();
    }

}