package com.sms.satp.utils;

import com.sms.satp.entity.Header;
import com.sms.satp.entity.Parameter;
import com.sms.satp.parser.model.ApiParameter;

import java.util.Objects;
import java.util.function.Function;

public abstract class ApiParameterConverter {

    private ApiParameterConverter() {
    }

    public static final Function<ApiParameter, Parameter> CONVERT_TO_PARAMETER =
            ApiParameterConverter::apiParameterConvertToParameter;

    public static final Function<ApiParameter, Header> CONVERT_TO_HEADER =
            ApiParameterConverter::apiParameterConvertToHeader;

    private static Parameter apiParameterConvertToParameter(ApiParameter apiParameter) {
        return Objects.nonNull(apiParameter)
                ? Parameter.builder()
                .name(apiParameter.getName())
                .type(apiParameter.getSchemaType())
                .description(apiParameter.getDescription())
                .required(apiParameter.getRequired())
                .deprecated(apiParameter.getDeprecated())
                .example(apiParameter.getExample())
                .build() : null;
    }

    private static Header apiParameterConvertToHeader(ApiParameter apiParameter) {
        return Objects.nonNull(apiParameter)
                ? Header.builder()
                .name(apiParameter.getName())
                .description(apiParameter.getDescription())
                .required(apiParameter.getRequired())
                .build() : null;
    }
}
