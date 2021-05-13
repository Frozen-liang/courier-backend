package com.sms.satp.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.common.enums.ParamType;
import com.sms.satp.dto.request.ParamInfoRequest;
import com.sms.satp.dto.response.ParamInfoResponse;
import com.sms.satp.entity.api.common.ParamInfo;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for ApiTagMapper")
public class ParamInfoMapperTest {

    private final ParamInfoMapper paramInfoMapper = new ParamInfoMapperImpl();
    private final ParamInfo paramInfo =
        ParamInfo.builder().paramType(ParamType.STRING).description(DESCRIPTION).build();
    private static final Integer SIZE = 10;
    private static final String DESCRIPTION = "description";

    @Test
    @DisplayName("Test the method to convert the ParamInfo's dto object to a entity object")
    void entity_to_dto() {
        ParamInfo paramInfo = ParamInfo.builder().description(DESCRIPTION).paramType(ParamType.OBJECT).build();
        ParamInfoResponse paramInfoDto = paramInfoMapper.toDto(paramInfo);
        assertThat(paramInfoDto.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(paramInfoDto.getParamType()).isEqualTo(ParamType.OBJECT.getCode());
    }

    @Test
    @DisplayName("Test the method to convert the ParamInfo's dto object to a entity object")
    void dto_to_entity() {
        ParamInfoRequest paramInfoDto = ParamInfoRequest.builder()
            .description(DESCRIPTION).paramType(1).build();
        ParamInfo paramInfo = paramInfoMapper.toEntity(paramInfoDto);
        assertThat(paramInfo.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(paramInfo.getParamType().getCode()).isEqualTo(1);
    }

    @Test
    @DisplayName("Test the method for converting an ParamInfo entity list object to a dto list object")
    void paramInfoList_to_paramInfoDtoList() {
        List<ParamInfo> paramInfos = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            paramInfos.add(paramInfo);
        }
        List<ParamInfoResponse> paramInfoDtoList = paramInfoMapper.toDtoList(paramInfos);
        assertThat(paramInfoDtoList).hasSize(SIZE);
        assertThat(paramInfoDtoList).allMatch(result -> StringUtils.equals(result.getDescription(), DESCRIPTION));
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the ParamInfo's entity object to a dto object")
    void null_entity_to_dto() {
        ParamInfo paramInfo = paramInfoMapper.toEntity(null);
        assertThat(paramInfo).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the ParamInfo's dto object to a entity object")
    void null_dto_to_entity() {
        ParamInfoResponse paramInfoDto = paramInfoMapper.toDto(null);
        assertThat(paramInfoDto).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method for converting an ParamInfo entity list object to a dto list "
        + "object")
    void null_entityList_to_dtoList() {
        List<ParamInfoResponse> paramInfoDtoList = paramInfoMapper.toDtoList(null);
        assertThat(paramInfoDtoList).isNull();
    }

}
