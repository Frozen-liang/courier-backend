package com.sms.courier.mapper;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.google.common.collect.Lists;
import com.sms.courier.dto.request.ProjectRequest;
import com.sms.courier.dto.response.MatchParamInfoResponse;
import com.sms.courier.dto.response.ProjectResponse;
import com.sms.courier.dto.response.ResponseResultVerificationResponse;
import com.sms.courier.entity.api.common.MatchParamInfo;
import com.sms.courier.entity.api.common.ResponseResultVerification;
import com.sms.courier.entity.project.ProjectEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for ResponseHeadersVerificationMapper")
public class ResponseResultVerificationMapperTest {

    private MatchParamInfoMapper matchParamInfoMapper = mock(MatchParamInfoMapper.class);
    private final ResponseResultVerificationMapper responseResultVerificationMapper =
            new ResponseResultVerificationMapperImpl(matchParamInfoMapper);

    private static final Integer SIZE = 10;
    private static final String NAME = "name";

    @Test
    @DisplayName("Test the method to convert the Project's entity object to a dto object")
    void toDto_Test(){
        ResponseResultVerification responseResultVerification = ResponseResultVerification.builder()
                .params(Lists.newArrayList(MatchParamInfo.builder().build()))
                .build();
        ResponseResultVerificationResponse dto = responseResultVerificationMapper.toDto(responseResultVerification);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the Project's entity object to a dto object")
    void toDto_IsNull_Test(){
        ResponseResultVerificationResponse dto = responseResultVerificationMapper.toDto(null);
        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Test the method to convert the Project's entity object to a dto object")
    void toEntity_Test(){
        ResponseResultVerificationResponse responseResultVerification = ResponseResultVerificationResponse.builder()
                .apiResponseJsonType(0)
                .verificationElementType(0)
                .params(Lists.newArrayList(MatchParamInfoResponse.builder().build()))
                .build();
        ResponseResultVerification dto = responseResultVerificationMapper.toEntity(responseResultVerification);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the Project's entity object to a dto object")
    void matchParamInfoListToMatchParamInfoResponseList_IsNull_Test(){
        List<MatchParamInfo> list = null;
        ResponseResultVerification responseResultVerification = ResponseResultVerification.builder()
                .params(list)
                .build();
        ResponseResultVerificationResponse dto = responseResultVerificationMapper.toDto(responseResultVerification);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the Project's entity object to a dto object")
    void matchParamInfoResponseListToMatchParamInfoList_IsNull_Test(){
        List<MatchParamInfoResponse> list = null;
        ResponseResultVerificationResponse responseResultVerification = ResponseResultVerificationResponse.builder()
                .apiResponseJsonType(0)
                .verificationElementType(0)
                .params(list)
                .build();
        ResponseResultVerification dto = responseResultVerificationMapper.toEntity(responseResultVerification);
        assertThat(dto).isNotNull();
    }

}
