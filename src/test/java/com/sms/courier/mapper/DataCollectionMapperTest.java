package com.sms.courier.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.courier.dto.request.DataCollectionRequest;
import com.sms.courier.dto.request.DataParamRequest;
import com.sms.courier.dto.request.TestDataRequest;
import com.sms.courier.dto.response.DataCollectionResponse;
import com.sms.courier.entity.datacollection.DataCollectionEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.sms.courier.entity.datacollection.DataParam;
import com.sms.courier.entity.datacollection.TestData;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
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
        DataCollectionEntity dataCollection = DataCollectionEntity.builder()
                .paramList(Lists.newArrayList())
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
        List<DataCollectionEntity> dataCollections = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            dataCollections.add(DataCollectionEntity.builder().collectionName(COLLECTION_NAME).build());
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
            .paramList(Lists.newArrayList())
            .collectionName(COLLECTION_NAME)
            .build();
        DataCollectionEntity dataCollection = dataCollectionMapper.toEntity(dataCollectionDto);
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
        DataCollectionEntity dataCollection = dataCollectionMapper.toEntity(null);
        assertThat(dataCollection).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method for converting an DataCollection entity list object to a dto list object")
    void null_entityList_to_dtoList() {
        List<DataCollectionResponse> dataCollectionDtoList = dataCollectionMapper.toDtoList(null);
        assertThat(dataCollectionDtoList).isNull();
    }

    @Test
    @DisplayName("Test the method to convert the DataCollection's dto object to a entity object")
    void testDataRequestListToTestDataListTest(){
        List<TestDataRequest>  list=Lists.newArrayList();
        DataCollectionRequest request=DataCollectionRequest.builder()
                .dataList(Lists.newArrayList(list))
                .build();
        assertThat(dataCollectionMapper.toEntity(request)).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the DataCollection's dto object to a entity object")
    void testDataRequestToTestDataTest(){
        TestDataRequest  testDataRequest=TestDataRequest.builder()
                .dataName("ddd")
                .data(Lists.newArrayList(DataParamRequest.builder().build()))
                .build();
        DataCollectionRequest dataCollectionDto=DataCollectionRequest.builder()
                .dataList(Lists.newArrayList(testDataRequest))
                .build();
        DataCollectionEntity dto = dataCollectionMapper.toEntity(dataCollectionDto);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the DataCollection's dto object to a entity object")
    void testDataRequestToTestDataIsNull_Test(){
        TestDataRequest  testDataRequest=null;
        DataCollectionRequest dataCollectionDto=DataCollectionRequest.builder()
                .dataList(Lists.newArrayList(testDataRequest))
                .build();
        DataCollectionEntity dto = dataCollectionMapper.toEntity(dataCollectionDto);
        assertThat(dto.getDataList()).size().isEqualTo(1);
    }

    @Test
    @DisplayName("Test the method to convert the DataCollection's dto object to a entity object")
    void testDataRequestToTestData_DataParamRequestIsNull_Test(){
        TestDataRequest  testDataRequest=TestDataRequest.builder()
                .dataName("ddd")
                .data(null)
                .build();
        DataCollectionRequest dataCollectionDto=DataCollectionRequest.builder()
                .dataList(Lists.newArrayList(testDataRequest))
                .build();
        DataCollectionEntity dto = dataCollectionMapper.toEntity(dataCollectionDto);
        assertThat(dto.getDataList()).size().isEqualTo(1);
    }

    @Test
    @DisplayName("Test the method to convert the DataCollection's dto object to a entity object")
    void testDataRequestToTestData_DataParamRequestNull_Test(){
        DataParamRequest request = null;
        TestDataRequest  testDataRequest=TestDataRequest.builder()
                .dataName("ddd")
                .data(Lists.newArrayList(request))
                .build();
        DataCollectionRequest dataCollectionDto=DataCollectionRequest.builder()
                .dataList(Lists.newArrayList(testDataRequest))
                .build();
        DataCollectionEntity dto = dataCollectionMapper.toEntity(dataCollectionDto);
        assertThat(dto.getDataList()).size().isEqualTo(1);
    }

    @Test
    @DisplayName("Test the method to convert the DataCollection's dto object to a entity object")
    void testDataListToTestDataResponseListTest(){
        List<TestData> list=Lists.newArrayList();
        DataCollectionEntity dataCollection=DataCollectionEntity.builder()
                .dataList(list)
                .build();
        assertThat(dataCollectionMapper.toDto(dataCollection)).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the DataCollection's dto object to a entity object")
    void testDataToTestDataResponseTest(){
        TestData testData=TestData.builder()
                .dataName("123")
                .data(Lists.newArrayList(DataParam.builder().build()))
                .build();
        DataCollectionEntity dataCollection=DataCollectionEntity.builder()
                .dataList(Lists.newArrayList(testData))
                .build();
        assertThat(dataCollectionMapper.toDto(dataCollection)).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the DataCollection's dto object to a entity object")
    void dataParamToDataParamResponse_isNull_Test(){
        DataParam dataParam=null;
        TestData testData=TestData.builder()
                .dataName("null")
                .data(Lists.newArrayList(dataParam))
                .build();
        DataCollectionEntity dataCollection=DataCollectionEntity.builder()
                .dataList(Lists.newArrayList(testData))
                .build();
        assertThat(dataCollectionMapper.toDto(dataCollection).getDataList()).size().isEqualTo(1);
    }

    @Test
    @DisplayName("Test the method to convert the DataCollection's dto object to a entity object")
    void dataParamListToDataParamResponseList_isNull_Test(){
        TestData testData=TestData.builder()
                .dataName("null")
                .data(null)
                .build();
        DataCollectionEntity dataCollection=DataCollectionEntity.builder()
                .dataList(Lists.newArrayList(testData))
                .build();
        assertThat(dataCollectionMapper.toDto(dataCollection).getDataList()).size().isEqualTo(1);
    }

    @Test
    @DisplayName("Test the method to convert the DataCollection's dto object to a entity object")
    void testDataToTestDataResponse_isNull_Test(){
        TestData testData=null;
        DataCollectionEntity dataCollection=DataCollectionEntity.builder()
                .dataList(Lists.newArrayList(testData))
                .build();
        assertThat(dataCollectionMapper.toDto(dataCollection).getDataList()).size().isEqualTo(1);
    }

}