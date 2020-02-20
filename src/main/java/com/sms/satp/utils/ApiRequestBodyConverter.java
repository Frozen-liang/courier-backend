package com.sms.satp.utils;

import com.sms.satp.entity.RequestBody;
import com.sms.satp.entity.RequestBody.RequestBodyBuilder;
import com.sms.satp.parser.common.MediaType;
import com.sms.satp.parser.model.ApiRequestBody;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

public abstract class ApiRequestBodyConverter {

    private ApiRequestBodyConverter() {
    }

    public static final Function<ApiRequestBody, RequestBody> CONVERT_TO_REQUEST_BODY =
        ApiRequestBodyConverter::apiRequestBodyConvert;

    private static RequestBody apiRequestBodyConvert(ApiRequestBody apiRequestBody) {
        if (Objects.nonNull(apiRequestBody)) {
            RequestBodyBuilder requestBodyBuilder = RequestBody.builder()
                .description(apiRequestBody.getDescription())
                .mediaTypes(null)
                .schema(ApiSchemaUtil.CONVERT_TO_SCHEMA.apply(apiRequestBody.getSchema()))
                .required(apiRequestBody.getRequired());
            if (CollectionUtils.isNotEmpty(apiRequestBody.getMediaTypes())) {
                requestBodyBuilder.mediaTypes(apiRequestBody.getMediaTypes()
                    .stream().map(MediaType::getType).collect(Collectors.toList()));
            }
            return requestBodyBuilder.build();
        } else {
            return null;
        }


    }
}