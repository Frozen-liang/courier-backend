package com.sms.satp.utils;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.entity.Header;
import com.sms.satp.parser.model.ApiHeader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ApiHeaderConverterTest {

    private static final String NAME = "api header name";
    private static final String DESCRIPTION = "api header description";

    @Test
    @DisplayName("Convert the ApiHeader list to an Header list")
    void test_CONVERT_TO_HEADER() {
        List<ApiHeader> apiHeaders = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            apiHeaders.add(ApiHeader.builder()
                .name(NAME)
                .description(DESCRIPTION)
                .build());
        }
        List<Header> headers = ApiHeaderConverter.CONVERT_TO_HEADER.apply(apiHeaders);
        assertThat(headers).allMatch(header -> StringUtils.equals(header.getName(), NAME));
        assertThat(headers).allMatch(header -> StringUtils.equals(header.getDescription(), DESCRIPTION));
    }

}