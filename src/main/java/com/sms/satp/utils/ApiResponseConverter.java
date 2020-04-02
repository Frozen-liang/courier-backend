package com.sms.satp.utils;

import com.sms.satp.entity.Response;
import com.sms.satp.entity.Response.ResponseBuilder;
import com.sms.satp.parser.common.MediaType;
import com.sms.satp.parser.model.ApiResponse;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;

public abstract class ApiResponseConverter {

    private ApiResponseConverter() {
    }

    public static final Function<ApiResponse, Response> CONVERT_TO_RESPONSE =
        ApiResponseConverter::apiResponseConvert;

    private static Response apiResponseConvert(ApiResponse apiResponse) {
        if (Objects.nonNull(apiResponse)) {
            ResponseBuilder responseBuilder = Response.builder()
                .id(new ObjectId().toString())
                .description(apiResponse.getDescription())
                .headers(ApiHeaderConverter.CONVERT_TO_HEADER.apply(apiResponse.getHeaders()))
                .mediaTypes(null)
                .schema(ApiSchemaUtil.CONVERT_TO_SCHEMA.apply(apiResponse.getSchema()));
            List<MediaType> mediaTypes = apiResponse.getMediaTypes();
            if (CollectionUtils.isNotEmpty(mediaTypes)) {
                responseBuilder.mediaTypes(mediaTypes.stream().filter(Objects::nonNull).map(MediaType::getType).collect(Collectors.toList()));
            }
            return responseBuilder.build();
        } else {
            return null;
        }
    }
}