package com.sms.satp.utils;

import com.sms.satp.entity.RequestBody;
import com.sms.satp.parser.common.MediaType;
import com.sms.satp.parser.model.ApiRequestBody;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class ApiRequestBodyConverter {

    private ApiRequestBodyConverter() {
    }

    public static final Function<ApiRequestBody, RequestBody> CONVERT_TO_REQUEST_BODY =
            ApiRequestBodyConverter::apiRequestBodyConvert;

    private static RequestBody apiRequestBodyConvert(ApiRequestBody apiRequestBody) {
        return Objects.nonNull(apiRequestBody)
                ? RequestBody.builder()
                .description(apiRequestBody.getDescription())
                .mediaTypes(
                        Optional.ofNullable(apiRequestBody.getMediaTypes()).isPresent() ? apiRequestBody.getMediaTypes()
                                .stream().map(MediaType::getType).collect(Collectors.toList()) : null)
                .schema(ApiSchemaUtil.CONVERT_TO_SCHEMA.apply(apiRequestBody.getSchema()))
                .required(apiRequestBody.getRequired())
                .build() : null;
    }
}