package com.sms.satp.utils;

import com.sms.satp.entity.Response;
import com.sms.satp.parser.common.MediaType;
import com.sms.satp.parser.model.ApiResponse;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class ApiResponseConverter {

    private ApiResponseConverter() {
    }

    public static final Function<ApiResponse, Response> CONVERT_TO_RESPONSE =
            ApiResponseConverter::apiResponseConvert;

    private static Response apiResponseConvert(ApiResponse apiResponse) {
        return Objects.nonNull(apiResponse)
                ? Response.builder()
                .description(apiResponse.getDescription())
                .headers(ApiHeaderConverter.CONVERT_TO_HEADER.apply(apiResponse.getHeaders()))
                .mediaTypes(
                        Optional.ofNullable(apiResponse.getMediaTypes()).isPresent() ? apiResponse.getMediaTypes()
                                .stream().map(MediaType::getType).collect(Collectors.toList()) : null)
                .schema(ApiSchemaUtil.CONVERT_TO_SCHEMA.apply(apiResponse.getSchema()))
                .build() : null;
    }
}