package com.sms.satp.parser.common;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

public enum MediaType {
    ALL("*/*"),
    APPLICATION_ATOM_XML("application/atom+xml"),
    APPLICATION_CBOR("application/cbor"),
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded"),
    APPLICATION_JSON("application/json"),
    APPLICATION_JSON_UTF8("application/json;charset=UTF-8"),
    APPLICATION_OCTET_STREAM("application/octet-stream"),
    APPLICATION_PDF("application/pdf"),
    APPLICATION_PROBLEM_JSON("application/problem+json"),
    APPLICATION_PROBLEM_JSON_UTF8("application/problem+json;charset=UTF-8"),
    APPLICATION_PROBLEM_XML("application/problem+xml"),
    APPLICATION_RSS_XML("application/rss+xml"),
    APPLICATION_STREAM_JSON("application/stream+json"),
    APPLICATION_XHTML_XML("application/xhtml+xml"),
    APPLICATION_XML("application/xml"),
    MULTIPART_FORM_DATA("multipart/form-data"),
    TEXT_EVENT_STREAM("text/event-stream"),
    TEXT_HTML_VALUE("text/html"),
    TEXT_MARKDOWN_VALUE("text/markdown");

    private String type;
    private static final Map<String, MediaType> mappings = new HashMap<>(16);

    static {
        for (MediaType mediaType : values()) {
            mappings.put(mediaType.type, mediaType);
        }
    }


    @Nullable
    public static MediaType resolve(@Nullable String type) {
        return (StringUtils.isNoneBlank(type) ? mappings.get(type) : null);
    }


    public boolean matches(String type) {
        return (this == resolve(type));
    }

    MediaType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
