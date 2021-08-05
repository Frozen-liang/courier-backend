package com.sms.courier.mapper;

import com.sms.courier.common.enums.MatchType;
import com.sms.courier.common.enums.ParamType;
import com.sms.courier.dto.response.MatchParamInfoResponse;
import com.sms.courier.entity.api.common.MatchParamInfo;
import com.sms.courier.entity.log.LogEntity;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.sms.courier.common.enums.OperationModule.PROJECT;
import static com.sms.courier.common.enums.OperationType.ADD;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests for MatchParamInfoMapperTest")
public class MatchParamInfoMapperTest {
    private final MatchParamInfoMapper matchParamInfoMapper = new MatchParamInfoMapperImpl();
    private final LogEntity logEntity =
            LogEntity.builder().operationType(ADD).operationModule(PROJECT).operationDesc(DESCRIPTION).build();
    private static final Integer SIZE = 10;
    private static final String DESCRIPTION = "description";

    @Test
    @DisplayName("Test the method to convert the ParamInfo's dto object to a entity object")
    void toDto_Test() {
        MatchParamInfo matchParamInfo = MatchParamInfo.builder()
                .childParam(Lists.newArrayList())
                .build();
        MatchParamInfoResponse dto = matchParamInfoMapper.toDto(matchParamInfo);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the ParamInfo's dto object to a entity object")
    void toDto_IsNull_Test() {
        MatchParamInfoResponse dto = matchParamInfoMapper.toDto(null);
        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Test the method to convert the ParamInfo's dto object to a entity object")
    void matchParamInfoListToMatchParamInfoResponseListIsNull_Test() {
        List<MatchParamInfo> list = null;
        MatchParamInfo matchParamInfo = MatchParamInfo.builder()
                .childParam(list)
                .build();
        MatchParamInfoResponse dto = matchParamInfoMapper.toDto(matchParamInfo);
        assertThat(dto.getChildParam()).isNull();
    }

    @Test
    @DisplayName("Test the method to convert the ParamInfo's dto object to a entity object")
    void matchParamInfoListToMatchParamInfoResponseList_Test() {
        List<MatchParamInfo> list = Lists.newArrayList(MatchParamInfo.builder()
                .paramType(ParamType.INT).matchType(MatchType.REGEX_MATCH).build());
        MatchParamInfo matchParamInfo = MatchParamInfo.builder()
                .childParam(list)
                .build();
        MatchParamInfoResponse dto = matchParamInfoMapper.toDto(matchParamInfo);
        assertThat(dto.getChildParam()).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the ParamInfo's dto object to a entity object")
    void toEntity_Test() {
        MatchParamInfoResponse matchParamInfo = MatchParamInfoResponse.builder()
                .paramType(0)
                .matchType(0)
                .build();
        MatchParamInfo dto = matchParamInfoMapper.toEntity(matchParamInfo);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the ParamInfo's dto object to a entity object")
    void matchParamInfoResponseListToMatchParamInfoList_Test() {
        List<MatchParamInfoResponse> list = null;
        MatchParamInfoResponse matchParamInfo = MatchParamInfoResponse.builder()
                .childParam(list)
                .paramType(0)
                .build();
        MatchParamInfo dto = matchParamInfoMapper.toEntity(matchParamInfo);
        assertThat(dto.getChildParam()).isNull();
    }

    @Test
    @DisplayName("Test the method to convert the ParamInfo's dto object to a entity object")
    void matchParamInfoResponseListToMatchParamInfoList_IsNull_Test() {
        List<MatchParamInfoResponse> list = Lists.newArrayList(MatchParamInfoResponse.builder()
                .paramType(0).matchType(0).build());
        MatchParamInfoResponse matchParamInfo = MatchParamInfoResponse.builder()
                .paramType(0)
                .matchType(0)
                .childParam(list)
                .build();
        MatchParamInfo dto = matchParamInfoMapper.toEntity(matchParamInfo);
        assertThat(dto.getChildParam()).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the ParamInfo's dto object to a entity object")
    void toEntity_IsNull_Test() {
        MatchParamInfo dto = matchParamInfoMapper.toEntity(null);
        assertThat(dto).isNull();
    }





}
