package com.sms.satp.service;

import static com.sms.satp.common.ErrorCode.ADD_DATA_COLLECTION_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_DATA_COLLECTION_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_DATA_COLLECTION_ERROR;
import static com.sms.satp.common.ErrorCode.GET_DATA_COLLECTION_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_DATA_COLLECTION_LIST_ERROR;
import static com.sms.satp.common.ErrorCode.GET_DATA_COLLECTION_PARAM_LIST_BY_ID_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.datacollection.DataCollection;
import com.sms.satp.entity.dto.DataCollectionDto;
import com.sms.satp.mapper.DataCollectionMapper;
import com.sms.satp.repository.DataCollectionRepository;
import com.sms.satp.service.impl.DataCollectionServiceImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

@DisplayName("Tests for DataCollectionService")
class DataCollectionServiceTest {

    private final DataCollectionRepository dataCollectionRepository = mock(DataCollectionRepository.class);
    private final DataCollectionMapper dataCollectionMapper = mock(DataCollectionMapper.class);
    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final DataCollectionService dataCollectionService = new DataCollectionServiceImpl(
        dataCollectionRepository, dataCollectionMapper, mongoTemplate);
    private final DataCollection dataCollection = DataCollection.builder().id(ID).build();
    private final DataCollectionDto dataCollectionDto = DataCollectionDto.builder().id(ID).build();
    private static final String ID = ObjectId.get().toString();
    private static final String NOT_EXIST_ID = ObjectId.get().toString();
    private static final Integer TOTAL_ELEMENTS = 10;
    private static final String COLLECTION_NAME = "collectionName";
    private static final String PROJECT_ID = ObjectId.get().toString();

    @Test
    @DisplayName("Test the findById method in the DataCollection service")
    public void findById_test() {
        when(dataCollectionRepository.findById(ID)).thenReturn(Optional.of(dataCollection));
        when(dataCollectionMapper.toDto(dataCollection)).thenReturn(dataCollectionDto);
        DataCollectionDto result1 = dataCollectionService.findById(ID);
        DataCollectionDto result2 = dataCollectionService.findById(NOT_EXIST_ID);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(ID);
        assertThat(result2).isNull();
    }

    @Test
    @DisplayName("An exception occurred while getting DataCollection")
    public void findById_exception_test() {
        doThrow(new RuntimeException()).when(dataCollectionRepository).findById(ID);
        assertThatThrownBy(() -> dataCollectionService.findById(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_DATA_COLLECTION_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the add method in the DataCollection service")
    public void add_test() {
        when(dataCollectionMapper.toEntity(dataCollectionDto)).thenReturn(dataCollection);
        when(dataCollectionRepository.insert(any(DataCollection.class))).thenReturn(dataCollection);
        dataCollectionService.add(dataCollectionDto);
        verify(dataCollectionRepository, times(1)).insert(any(DataCollection.class));
    }

    @Test
    @DisplayName("An exception occurred while adding DataCollection")
    public void add_exception_test() {
        when(dataCollectionMapper.toEntity(any())).thenReturn(DataCollection.builder().build());
        doThrow(new RuntimeException()).when(dataCollectionRepository).insert(any(DataCollection.class));
        assertThatThrownBy(() -> dataCollectionService.add(dataCollectionDto))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_DATA_COLLECTION_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the DataCollection service")
    public void edit_test() {
        when(dataCollectionMapper.toEntity(dataCollectionDto)).thenReturn(dataCollection);
        when(dataCollectionRepository.findById(any()))
            .thenReturn(Optional.of(DataCollection.builder().id(ID).build()));
        when(dataCollectionRepository.save(any(DataCollection.class))).thenReturn(dataCollection);
        dataCollectionService.edit(dataCollectionDto);
        verify(dataCollectionRepository, times(1)).save(any(DataCollection.class));
    }

    @Test
    @DisplayName("An exception occurred while edit DataCollection")
    public void edit_exception_test() {
        when(dataCollectionMapper.toEntity(dataCollectionDto)).thenReturn(dataCollection);
        when(dataCollectionRepository.findById(any())).thenReturn(Optional.of(dataCollection));
        doThrow(new RuntimeException()).when(dataCollectionRepository).save(any(DataCollection.class));
        assertThatThrownBy(() -> dataCollectionService.edit(dataCollectionDto))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_DATA_COLLECTION_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the DataCollection service")
    public void list_test() {
        ArrayList<DataCollection> dataCollectionList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            dataCollectionList.add(DataCollection.builder().build());
        }
        ArrayList<DataCollectionDto> dataCollectionDtoList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            dataCollectionDtoList.add(DataCollectionDto.builder().build());
        }
        when(dataCollectionRepository.findAll(any(), any(Sort.class))).thenReturn(dataCollectionList);
        when(dataCollectionMapper.toDtoList(dataCollectionList)).thenReturn(dataCollectionDtoList);
        List<DataCollectionDto> result = dataCollectionService.list(PROJECT_ID, COLLECTION_NAME);
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting DataCollection list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(dataCollectionRepository).findAll(any(), any(Sort.class));
        assertThatThrownBy(() -> dataCollectionService.list(PROJECT_ID, COLLECTION_NAME))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_DATA_COLLECTION_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the DataCollection service")
    public void delete_test() {
        dataCollectionService.delete(new String[]{ID});
        verify(mongoTemplate, times(1))
            .updateMulti(any(Query.class), any(UpdateDefinition.class), any(Class.class));
    }

    @Test
    @DisplayName("An exception occurred while delete DataCollection")
    public void delete_exception_test() {
        doThrow(new RuntimeException()).when(mongoTemplate)
            .updateMulti(any(Query.class), any(UpdateDefinition.class), any(Class.class));
        assertThatThrownBy(() -> dataCollectionService.delete(new String[]{ID}))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_DATA_COLLECTION_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the getParamList method in the DataCollection service")
    public void getParamList_test() {
        List<String> paramList = Arrays.asList("active", "city", "code");
        when(mongoTemplate.findOne(any(), any())).thenReturn(DataCollection.builder().paramList(paramList).build());
        List<String> result = dataCollectionService.getParamListById(ID);
        assertThat(result).hasSize(paramList.size());
    }

    @Test
    @DisplayName("An exception occurred while getting DataCollectionParamList")
    public void getParamList_exception_test() {
        doThrow(new RuntimeException()).when(mongoTemplate).findOne(any(), any());
        assertThatThrownBy(() -> dataCollectionService.getParamListById(ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_DATA_COLLECTION_PARAM_LIST_BY_ID_ERROR.getCode());
    }
}