package com.sms.satp.utils;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.entity.Header;
import com.sms.satp.entity.Parameter;
import com.sms.satp.parser.model.ApiParameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ApiParameterConverterTest {

    private static final String NAME = "name";

    @Test
    @DisplayName("Convert the ApiParameter to an Parameter")
    void CONVERT_TO_PARAMETER_test() {
        ApiParameter apiParameter = ApiParameter.builder().name(NAME).build();
        Parameter parameter =
            ApiParameterConverter.CONVERT_TO_PARAMETER.apply(apiParameter);
        assertThat(parameter.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Convert the ApiParameter to an Header")
    void CONVERT_TO_HEADER_test() {
        ApiParameter apiParameter = ApiParameter.builder().name(NAME).build();
        Header header = ApiParameterConverter.CONVERT_TO_HEADER.apply(apiParameter);
        assertThat(header.getName()).isEqualTo(NAME);
    }

}