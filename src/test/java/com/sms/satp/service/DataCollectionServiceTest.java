package com.sms.satp.service;

import static com.sms.satp.common.exception.ErrorCode.ADD_DATA_COLLECTION_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_DATA_COLLECTION_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_DATA_COLLECTION_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_DATA_COLLECTION_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_DATA_COLLECTION_LIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_DATA_COLLECTION_PARAM_LIST_BY_ID_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.DataCollectionImportRequest;
import com.sms.satp.dto.request.DataCollectionRequest;
import com.sms.satp.dto.response.DataCollectionResponse;
import com.sms.satp.entity.datacollection.DataCollectionEntity;
import com.sms.satp.mapper.DataCollectionMapper;
import com.sms.satp.repository.CustomizedDataCollectionRepository;
import com.sms.satp.repository.DataCollectionRepository;
import com.sms.satp.service.impl.DataCollectionServiceImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

@DisplayName("Tests for DataCollectionService")
class DataCollectionServiceTest {

    private final DataCollectionRepository dataCollectionRepository = mock(DataCollectionRepository.class);
    private final DataCollectionMapper dataCollectionMapper = mock(DataCollectionMapper.class);
    private final CustomizedDataCollectionRepository customizedDataCollectionRepository = mock(
        CustomizedDataCollectionRepository.class);
    private final DataCollectionService dataCollectionService = new DataCollectionServiceImpl(
        dataCollectionRepository, dataCollectionMapper, customizedDataCollectionRepository);
    private final DataCollectionEntity dataCollection = DataCollectionEntity.builder().id(ID).build();
    private final DataCollectionResponse dataCollectionResponse = DataCollectionResponse.builder().id(ID).build();
    private final DataCollectionRequest dataCollectionRequest = DataCollectionRequest.builder().id(ID).build();
    private static final String ID = ObjectId.get().toString();
    private static final Integer TOTAL_ELEMENTS = 10;
    private static final String COLLECTION_NAME = "collectionName";
    private static final String PROJECT_ID = ObjectId.get().toString();

    @Test
    @DisplayName("Test the findById method in the DataCollection service")
    public void findById_test() {
        when(dataCollectionRepository.findById(ID)).thenReturn(Optional.of(dataCollection));
        when(dataCollectionMapper.toDto(dataCollection)).thenReturn(dataCollectionResponse);
        DataCollectionResponse result1 = dataCollectionService.findById(ID);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("Test the findOne method in the DataCollection service")
    public void findOne_test() {
        when(dataCollectionRepository.findById(ID)).thenReturn(Optional.of(dataCollection));
        DataCollectionEntity result = dataCollectionService.findOne(ID);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(ID);
    }


    @Test
    @DisplayName("An exception occurred while getting DataCollection")
    public void findById_exception_test() {
        when(dataCollectionRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> dataCollectionService.findById(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_DATA_COLLECTION_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the add method in the DataCollection service")
    public void add_test() {
        when(dataCollectionMapper.toEntity(dataCollectionRequest)).thenReturn(dataCollection);
        when(dataCollectionRepository.insert(any(DataCollectionEntity.class))).thenReturn(dataCollection);
        dataCollectionService.add(dataCollectionRequest);
        verify(dataCollectionRepository, times(1)).insert(any(DataCollectionEntity.class));
    }

    @Test
    @DisplayName("An exception occurred while adding DataCollection")
    public void add_exception_test() {
        when(dataCollectionMapper.toEntity(any())).thenReturn(DataCollectionEntity.builder().build());
        doThrow(new RuntimeException()).when(dataCollectionRepository).insert(any(DataCollectionEntity.class));
        assertThatThrownBy(() -> dataCollectionService.add(dataCollectionRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_DATA_COLLECTION_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the DataCollection service")
    public void edit_test() {
        when(dataCollectionMapper.toEntity(dataCollectionRequest)).thenReturn(dataCollection);
        when(dataCollectionRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(dataCollectionRepository.save(any(DataCollectionEntity.class))).thenReturn(dataCollection);
        assertThat(dataCollectionService.edit(dataCollectionRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while edit DataCollection")
    public void edit_exception_test() {
        when(dataCollectionMapper.toEntity(dataCollectionRequest)).thenReturn(dataCollection);
        when(dataCollectionRepository.existsById(any())).thenReturn(Boolean.TRUE);
        doThrow(new RuntimeException()).when(dataCollectionRepository).save(any(DataCollectionEntity.class));
        assertThatThrownBy(() -> dataCollectionService.edit(dataCollectionRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_DATA_COLLECTION_ERROR.getCode());
    }

    @Test
    @DisplayName("An not exist exception occurred while edit DataCollection")
    public void edit_not_exist_exception_test() {
        when(dataCollectionMapper.toEntity(dataCollectionRequest)).thenReturn(dataCollection);
        when(dataCollectionRepository.existsById(any())).thenReturn(Boolean.FALSE);
        assertThatThrownBy(() -> dataCollectionService.edit(dataCollectionRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the DataCollection service")
    public void list_test() {
        ArrayList<DataCollectionEntity> dataCollectionList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            dataCollectionList.add(DataCollectionEntity.builder().build());
        }
        ArrayList<DataCollectionResponse> dataCollectionDtoList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            dataCollectionDtoList.add(DataCollectionResponse.builder().build());
        }
        when(dataCollectionRepository.findAll(any(), any(Sort.class))).thenReturn(dataCollectionList);
        when(dataCollectionMapper.toDtoList(dataCollectionList)).thenReturn(dataCollectionDtoList);
        List<DataCollectionResponse> result = dataCollectionService.list(PROJECT_ID, COLLECTION_NAME);
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
        List<String> ids = Collections.singletonList(ID);
        when(customizedDataCollectionRepository.deleteByIds(ids)).thenReturn(Boolean.TRUE);
        assertThat(dataCollectionService.delete(Collections.singletonList(ID))).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete DataCollection")
    public void delete_exception_test() {
        List<String> ids = Collections.singletonList(ID);
        doThrow(new RuntimeException()).when(customizedDataCollectionRepository)
            .deleteByIds(ids);
        assertThatThrownBy(() -> dataCollectionService.delete(ids))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_DATA_COLLECTION_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the getParamList method in the DataCollection service")
    public void getParamList_test() {
        List<String> paramList = Arrays.asList("active", "city", "code");
        when(customizedDataCollectionRepository.getParamListById(any())).thenReturn(paramList);
        List<String> result = dataCollectionService.getParamListById(ID);
        assertThat(result).hasSize(paramList.size());
    }

    @Test
    @DisplayName("An exception occurred while getting DataCollectionParamList")
    public void getParamList_exception_test() {
        doThrow(new RuntimeException()).when(customizedDataCollectionRepository).getParamListById(any());
        assertThatThrownBy(() -> dataCollectionService.getParamListById(ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_DATA_COLLECTION_PARAM_LIST_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the importDataCollection method in the DataCollection service")
    public void importDataCollection_test() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("/datacollection/template.csv");
        MultipartFile multipartFile = mock(MultipartFile.class);
        DataCollectionImportRequest dataCollectionImportRequest = DataCollectionImportRequest.builder().importMode(1)
            .file(multipartFile)
            .build();
        when(dataCollectionRepository.findById(any())).thenReturn(Optional.of(DataCollectionEntity.builder().build()));
        when(multipartFile.getInputStream()).thenReturn(classPathResource.getInputStream());
        when(dataCollectionRepository.save(any())).thenReturn(null);
        assertThat(dataCollectionService.importDataCollection(dataCollectionImportRequest)).isTrue();
    }
}