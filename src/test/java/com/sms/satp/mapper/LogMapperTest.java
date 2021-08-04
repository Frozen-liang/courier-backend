package com.sms.satp.mapper;

import static com.sms.satp.common.enums.OperationModule.PROJECT;
import static com.sms.satp.common.enums.OperationType.ADD;
import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.dto.response.LogResponse;
import com.sms.satp.entity.log.LogEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for LogMapper")
public class LogMapperTest {

    private final LogMapper logMapper = new LogMapperImpl();
    private final LogEntity logEntity =
            LogEntity.builder().operationType(ADD).operationModule(PROJECT)
                    .operationDesc(DESCRIPTION).createDateTime(LocalDateTime.now()).build();
    private static final Integer SIZE = 10;
    private static final String DESCRIPTION = "description";

    @Test
    @DisplayName("Test the method to convert the ParamInfo's dto object to a entity object")
    void entity_to_dto() {
        LogResponse logResponse = logMapper.toDto(logEntity);
        assertThat(logResponse.getOperationDesc()).isEqualTo(DESCRIPTION);
        assertThat(logResponse.getOperationType()).isEqualTo(ADD.getCode());
    }

    @Test
    @DisplayName("Test the method to convert the ParamInfo's dto object to a entity object")
    void entity_to_dto_isNull() {
        assertThat(logMapper.toDto(null)).isNull();
    }

    @Test
    @DisplayName("Test the method for converting an ParamInfo entity list object to a dto list object")
    void paramInfoList_to_paramInfoDtoList() {
        List<LogEntity> logList = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            logList.add(logEntity);
        }
        List<LogResponse> logResponseList = logMapper.toDtoList(logList);
        assertThat(logResponseList).hasSize(SIZE);
        assertThat(logResponseList).allMatch(result -> StringUtils.equals(result.getOperationDesc(), DESCRIPTION));
    }

    @Test
    @DisplayName("Test the method to convert the ParamInfo's dto object to a entity object")
    void toDtoList_isNull_Test() {
        assertThat(logMapper.toDtoList(null)).isNull();
    }

}
