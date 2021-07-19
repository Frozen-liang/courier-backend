package com.sms.satp.service.impl;

import static com.sms.satp.common.enums.OperationModule.DATA_COLLECTION;
import static com.sms.satp.common.enums.OperationType.ADD;
import static com.sms.satp.common.enums.OperationType.DELETE;
import static com.sms.satp.common.enums.OperationType.EDIT;
import static com.sms.satp.common.exception.ErrorCode.ADD_DATA_COLLECTION_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_DATA_COLLECTION_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_DATA_COLLECTION_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_DATA_COLLECTION_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_DATA_COLLECTION_LIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_DATA_COLLECTION_PARAM_LIST_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.IMPORT_DATA_COLLECTION_ERROR;
import static com.sms.satp.common.field.CommonFiled.CREATE_DATE_TIME;
import static com.sms.satp.common.field.CommonFiled.PROJECT_ID;
import static com.sms.satp.common.field.CommonFiled.REMOVE;
import static com.sms.satp.utils.Assert.isNull;
import static com.sms.satp.utils.Assert.isTrue;
import static com.sms.satp.utils.Assert.notEmpty;

import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.enums.ImportMode;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.DataCollectionImportRequest;
import com.sms.satp.dto.request.DataCollectionRequest;
import com.sms.satp.dto.response.DataCollectionResponse;
import com.sms.satp.entity.datacollection.DataCollectionEntity;
import com.sms.satp.entity.datacollection.DataParam;
import com.sms.satp.entity.datacollection.TestData;
import com.sms.satp.mapper.DataCollectionMapper;
import com.sms.satp.repository.CustomizedDataCollectionRepository;
import com.sms.satp.repository.DataCollectionRepository;
import com.sms.satp.service.DataCollectionService;
import com.sms.satp.utils.ExceptionUtils;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DataCollectionServiceImpl implements DataCollectionService {

    private static final String DELIMITER = ",";
    private final DataCollectionRepository dataCollectionRepository;
    private final DataCollectionMapper dataCollectionMapper;
    private final CustomizedDataCollectionRepository customizedDataCollectionRepository;

    public DataCollectionServiceImpl(DataCollectionRepository dataCollectionRepository,
        DataCollectionMapper dataCollectionMapper,
        CustomizedDataCollectionRepository customizedDataCollectionRepository) {
        this.dataCollectionRepository = dataCollectionRepository;
        this.dataCollectionMapper = dataCollectionMapper;
        this.customizedDataCollectionRepository = customizedDataCollectionRepository;
    }

    @Override
    public DataCollectionResponse findById(String id) {
        return dataCollectionRepository.findById(id).map(dataCollectionMapper::toDto)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_DATA_COLLECTION_BY_ID_ERROR));
    }

    @Override
    public DataCollectionEntity findOne(String id) {
        return dataCollectionRepository.findById(id).orElse(null);
    }

    @Override
    public List<DataCollectionResponse> list(String projectId, String collectionName) {
        try {
            Sort sort = Sort.by(Direction.DESC, CREATE_DATE_TIME.getFiled());
            DataCollectionEntity dataCollection = DataCollectionEntity.builder().projectId(projectId)
                .collectionName(collectionName)
                .build();
            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher(PROJECT_ID.getFiled(), GenericPropertyMatchers.exact())
                .withMatcher(REMOVE.getFiled(), GenericPropertyMatchers.exact())
                .withStringMatcher(StringMatcher.CONTAINING);
            Example<DataCollectionEntity> example = Example.of(dataCollection, exampleMatcher);
            return dataCollectionMapper.toDtoList(dataCollectionRepository.findAll(example, sort));
        } catch (Exception e) {
            log.error("Failed to get the DataCollection list!", e);
            throw new ApiTestPlatformException(GET_DATA_COLLECTION_LIST_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = DATA_COLLECTION,
        template = "{{#dataCollectionRequest.collectionName}}")
    public Boolean add(DataCollectionRequest dataCollectionRequest) {
        log.info("DataCollectionService-add()-params: [DataCollection]={}", dataCollectionRequest.toString());
        try {
            DataCollectionEntity dataCollection = dataCollectionMapper.toEntity(dataCollectionRequest);
            dataCollectionRepository.insert(dataCollection);
        } catch (Exception e) {
            log.error("Failed to add the DataCollection!", e);
            throw new ApiTestPlatformException(ADD_DATA_COLLECTION_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = DATA_COLLECTION,
        template = "{{#dataCollectionRequest.collectionName}}")
    public Boolean edit(DataCollectionRequest dataCollectionRequest) {
        log.info("DataCollectionService-edit()-params: [DataCollection]={}", dataCollectionRequest.toString());
        try {
            boolean exists = dataCollectionRepository.existsById(dataCollectionRequest.getId());
            isTrue(exists, EDIT_NOT_EXIST_ERROR, "DataCollection", dataCollectionRequest.getId());
            DataCollectionEntity dataCollection = dataCollectionMapper.toEntity(dataCollectionRequest);
            dataCollectionRepository.save(dataCollection);
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (Exception e) {
            log.error("Failed to add the DataCollection!", e);
            throw new ApiTestPlatformException(EDIT_DATA_COLLECTION_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = DATA_COLLECTION,
        template = "{{#result?.![#this.collectionName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean delete(List<String> ids) {
        try {
            return customizedDataCollectionRepository.deleteByIds(ids);
        } catch (Exception e) {
            log.error("Failed to delete the DataCollection!", e);
            throw new ApiTestPlatformException(DELETE_DATA_COLLECTION_BY_ID_ERROR);
        }
    }

    @Override
    public List<String> getParamListById(String id) {
        try {
            return customizedDataCollectionRepository.getParamListById(id);
        } catch (Exception e) {
            log.error("Failed to get the DataCollectionParamList by Id!", e);
            throw new ApiTestPlatformException(GET_DATA_COLLECTION_PARAM_LIST_BY_ID_ERROR);
        }
    }

    @Override
    public Boolean importDataCollection(DataCollectionImportRequest request) {
        try {
            Optional<DataCollectionEntity> optional = dataCollectionRepository.findById(request.getId());
            DataCollectionEntity dataCollection = optional
                .orElseThrow(() -> ExceptionUtils.mpe("The dataCollection not exits. id = %s", request.getId()));
            List<String> paramList = Objects.requireNonNullElse(dataCollection.getParamList(), new ArrayList<>());
            List<TestData> dataList = Objects.requireNonNullElse(dataCollection.getDataList(), new ArrayList<>());
            ImportMode importMode = ImportMode.getType(request.getImportMode());
            InputStream inputStream = request.getFile().getInputStream();
            List<String> sourceList = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
            notEmpty(sourceList, "The file is empty.");
            if (importMode == ImportMode.COVER) {
                paramList.clear();
                dataList.clear();
            }
            String[] keys = sourceList.get(0).split(DELIMITER);
            for (int i = 1; i < keys.length; i++) {
                if (!paramList.contains(keys[i])) {
                    paramList.add(keys[i]);
                }
            }
            for (int i = 1; i < sourceList.size(); i++) {
                String[] values = sourceList.get(i).split(DELIMITER);
                List<DataParam> dataParams = new ArrayList<>();
                for (int j = 1; j < values.length; j++) {
                    dataParams.add(DataParam.builder().key(keys[j]).value(values[j]).build());
                }
                TestData testData = TestData.builder().dataName(values[0]).data(dataParams).build();
                dataList.add(testData);
            }
            dataCollectionRepository.save(dataCollection);
            return Boolean.TRUE;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to import the DataCollection!", e);
            throw new ApiTestPlatformException(IMPORT_DATA_COLLECTION_ERROR);
        }
    }

}