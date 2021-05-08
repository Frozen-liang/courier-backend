package com.sms.satp.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public enum Media {
    ALL("*/*", ApiRequestParamType.JSON),
    APPLICATION_ATOM_XML("application/atom+xml", ApiRequestParamType.XML),
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded", ApiRequestParamType.FORM_DATA),
    APPLICATION_JSON("application/json", ApiRequestParamType.JSON),
    APPLICATION_JSON_UTF8("application/json;charset=UTF-8", ApiRequestParamType.JSON),
    APPLICATION_OCTET_STREAM("application/octet-stream", ApiRequestParamType.BINARY),
    APPLICATION_PDF("application/pdf", ApiRequestParamType.BINARY),
    APPLICATION_PROBLEM_JSON("application/problem+json", ApiRequestParamType.JSON),
    APPLICATION_PROBLEM_JSON_UTF8("application/problem+json;charset=UTF-8", ApiRequestParamType.JSON),
    APPLICATION_PROBLEM_XML("application/problem+xml", ApiRequestParamType.XML),
    APPLICATION_RSS_XML("application/rss+xml", ApiRequestParamType.XML),
    APPLICATION_STREAM_JSON("application/stream+json", ApiRequestParamType.JSON),
    APPLICATION_XHTML_XML("application/xhtml+xml", ApiRequestParamType.XML),
    APPLICATION_XML("application/xml", ApiRequestParamType.XML),
    MULTIPART_FORM_DATA("multipart/form-data", ApiRequestParamType.FORM_DATA),
    TEXT_EVENT_STREAM("text/event-stream", ApiRequestParamType.BINARY),
    TEXT_HTML_VALUE("text/html", ApiRequestParamType.RAW),
    TEXT_MARKDOWN_VALUE("text/markdown", ApiRequestParamType.BINARY);

    private final String type;
    private final ApiRequestParamType apiRequestParamType;
    private static final Map<String, Media> mappings = Arrays
        .stream(values()).collect(Collectors.toMap(Media::getType, Function.identity()));


    @NonNull
    public static Media resolve(@Nullable String type) {
        return mappings.getOrDefault(type, ALL);
    }

    public boolean matches(String type) {
        return (this == resolve(type));
    }

    public ApiRequestParamType getApiRequestParamType() {
        return apiRequestParamType;
    }

    public String getType() {
        return type;
    }

    Media(String type, ApiRequestParamType apiRequestParamType) {
        this.type = type;
        this.apiRequestParamType = apiRequestParamType;
    }

}
