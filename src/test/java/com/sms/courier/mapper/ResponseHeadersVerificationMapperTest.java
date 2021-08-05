package com.sms.courier.mapper;

import com.google.common.collect.Lists;
import com.sms.courier.dto.response.ResponseHeadersVerificationResponse;
import com.sms.courier.entity.api.common.MatchParamInfo;
import com.sms.courier.entity.api.common.ResponseHeadersVerification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("Tests for ResponseHeadersVerificationMapper")
public class ResponseHeadersVerificationMapperTest {

    private MatchParamInfoMapper matchParamInfoMapper = mock(MatchParamInfoMapper.class);
    private final ResponseHeadersVerificationMapper responseHeadersVerificationMapper =
            new ResponseHeadersVerificationMapperImpl(matchParamInfoMapper);

    @Test
    @DisplayName("Test the method to convert the Project's entity object to a dto object")
    void toDto_Test() {
        ResponseHeadersVerification responseHeadersVerification = ResponseHeadersVerification.builder()
                .params(Lists.newArrayList(MatchParamInfo.builder().build()))
                .build();
        ResponseHeadersVerificationResponse dto = responseHeadersVerificationMapper.toDto(responseHeadersVerification);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the Project's entity object to a dto object")
    void toDto_IsNull_Test() {
        ResponseHeadersVerificationResponse dto = responseHeadersVerificationMapper.toDto(null);
        assertThat(dto).isNull();
    }

}
