package com.sms.courier.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.courier.dto.request.DataStructureRequest;
import com.sms.courier.dto.response.DataStructureListResponse;
import com.sms.courier.dto.response.DataStructureReferenceResponse;
import com.sms.courier.dto.response.DataStructureResponse;
import com.sms.courier.entity.structure.StructureEntity;
import com.sms.courier.entity.structure.StructureRefRecordEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("Tests for DataStructureMapper")
class DataStructureMapperTest {

    private final ParamInfoMapper paramInfoMapper = new ParamInfoMapperImpl();
    private final DataStructureMapper dataStructureMapper = new DataStructureMapperImpl(paramInfoMapper);
    private static final Integer SIZE = 10;
    private static final String NAME = "dataStructure";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the DataStructure's entity object to a dto object")
    void entity_to_dto() {
        StructureEntity dataStructure = StructureEntity.builder()
            .name(NAME)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        DataStructureResponse dataStructureResponse = dataStructureMapper.toResponse(dataStructure);
        assertThat(dataStructureResponse.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Test the method to convert the DataStructure's entity object to a list response object")
    void toListResponse_test() {
        StructureEntity dataStructure = StructureEntity.builder()
            .name(NAME)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        DataStructureListResponse dataStructureListResponse = dataStructureMapper.toListResponse(dataStructure);
        assertThat(dataStructureListResponse.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Test the method for converting an DataStructure entity list object to a dto list object")
    void dataStructureList_to_dataStructureDtoList() {
        List<StructureEntity> dataStructures = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            dataStructures.add(StructureEntity.builder().name(NAME).build());
        }
        List<DataStructureResponse> dataStructureResponseList = dataStructureMapper.toResponseList(dataStructures);
        assertThat(dataStructureResponseList).hasSize(SIZE);
        assertThat(dataStructureResponseList).allMatch(result -> StringUtils.equals(result.getName(), NAME));
    }

    @Test
    @DisplayName("Test the method for converting an DataStructure entity list object to a list responses object")
    void toListResponses_test() {
        List<StructureEntity> dataStructures = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            dataStructures.add(StructureEntity.builder().name(NAME).build());
        }
        List<DataStructureListResponse> dataStructureListResponses = dataStructureMapper
            .toListResponses(dataStructures);
        assertThat(dataStructureListResponses).hasSize(SIZE);
        assertThat(dataStructureListResponses).allMatch(result -> StringUtils.equals(result.getName(), NAME));
    }


    @Test
    @DisplayName("Test the method to convert the DataStructure's dto object to a entity object")
    void dto_to_entity() {
        DataStructureRequest dataStructureRequest = DataStructureRequest.builder()
            .name(NAME)
            .build();
        StructureEntity dataStructure = dataStructureMapper.toEntity(dataStructureRequest);
        assertThat(dataStructure.getName()).isEqualTo(NAME);
    }


    @Test
    @DisplayName("Test the method for converting an DataStructure entity list object to a reference response list "
        + "object")
    void toReferenceResponse_test() {
        List<StructureRefRecordEntity> dataStructures = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            dataStructures.add(StructureRefRecordEntity.builder().name(NAME).build());
        }
        List<DataStructureReferenceResponse> referenceResponses = dataStructureMapper
            .toReferenceResponses(dataStructures);
        assertThat(referenceResponses).hasSize(SIZE);
        assertThat(referenceResponses).allMatch(result -> StringUtils.equals(result.getName(), NAME));
    }
}
