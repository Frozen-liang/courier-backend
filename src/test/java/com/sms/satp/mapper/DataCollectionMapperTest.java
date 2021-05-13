package com.sms.satp.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.dto.request.DataCollectionRequest;
import com.sms.satp.dto.response.DataCollectionResponse;
import com.sms.satp.entity.datacollection.DataCollection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for DataCollectionMapper")
class DataCollectionMapperTest {

    private DataCollectionMapper dataCollectionMapper = new DataCollectionMapperImpl();

    private static final Integer SIZE = 10;
    private static final String COLLECTION_NAME = "testName";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the DataCollection's entity object to a dto object")
    void entity_to_dto() {
        DataCollection dataCollection = DataCollection.builder()
            .collectionName(COLLECTION_NAME)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        DataCollectionResponse dataCollectionDto = dataCollectionMapper.toDto(dataCollection);
        assertThat(dataCollectionDto.getCollectionName()).isEqualTo(COLLECTION_NAME);
    }

    @Test
    @DisplayName("Test the method for converting an DataCollection entity list object to a dto list object")
    void dataCollectionList_to_dataCollectionDtoList() {
        List<DataCollection> dataCollections = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            dataCollections.add(DataCollection.builder().collectionName(COLLECTION_NAME).build());
        }
        List<DataCollectionResponse> dataCollectionDtoList = dataCollectionMapper.toDtoList(dataCollections);
        assertThat(dataCollectionDtoList).hasSize(SIZE);
        assertThat(dataCollectionDtoList).allMatch(result -> StringUtils
            .equals(result.getCollectionName(), COLLECTION_NAME));
    }

    @Test
    @DisplayName("Test the method to convert the DataCollection's dto object to a entity object")
    void dto_to_entity() {
        DataCollectionRequest dataCollectionDto = DataCollectionRequest.builder()
            .collectionName(COLLECTION_NAME)
            .build();
        DataCollection dataCollection = dataCollectionMapper.toEntity(dataCollectionDto);
        assertThat(dataCollection.getCollectionName()).isEqualTo(COLLECTION_NAME);
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the DataCollection's entity object to a dto object")
    void null_entity_to_dto() {
        DataCollectionResponse dataCollectionDto = dataCollectionMapper.toDto(null);
        assertThat(dataCollectionDto).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the DataCollection's dto object to a entity object")
    void null_dto_to_entity() {
        DataCollection dataCollection = dataCollectionMapper.toEntity(null);
        assertThat(dataCollection).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method for converting an DataCollection entity list object to a dto list object")
    void null_entityList_to_dtoList() {
        List<DataCollectionResponse> dataCollectionDtoList = dataCollectionMapper.toDtoList(null);
        assertThat(dataCollectionDtoList).isNull();
    }

}