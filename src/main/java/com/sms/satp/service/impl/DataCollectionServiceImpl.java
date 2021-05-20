package com.sms.satp.service.impl;

import static com.sms.satp.common.enums.OperationModule.DATA_COLLECTION;
import static com.sms.satp.common.enums.OperationType.ADD;
import static com.sms.satp.common.enums.OperationType.DELETE;
import static com.sms.satp.common.enums.OperationType.EDIT;
import static com.sms.satp.common.exception.ErrorCode.ADD_DATA_COLLECTION_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_DATA_COLLECTION_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_DATA_COLLECTION_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_DATA_COLLECTION_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_DATA_COLLECTION_LIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_DATA_COLLECTION_PARAM_LIST_BY_ID_ERROR;
import static com.sms.satp.common.field.CommonFiled.CREATE_DATE_TIME;
import static com.sms.satp.common.field.CommonFiled.PROJECT_ID;
import static com.sms.satp.common.field.CommonFiled.REMOVE;

import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.DataCollectionRequest;
import com.sms.satp.dto.response.DataCollectionResponse;
import com.sms.satp.entity.datacollection.DataCollection;
import com.sms.satp.mapper.DataCollectionMapper;
import com.sms.satp.repository.CustomizedDataCollectionRepository;
import com.sms.satp.repository.DataCollectionRepository;
import com.sms.satp.service.DataCollectionService;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
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
        try {
            Optional<DataCollection> optional = dataCollectionRepository.findById(id);
            return dataCollectionMapper.toDto(optional.orElse(null));
        } catch (Exception e) {
            log.error("Failed to get the DataCollection by id!", e);
            throw new ApiTestPlatformException(GET_DATA_COLLECTION_BY_ID_ERROR);
        }
    }

    @Override
    public List<DataCollectionResponse> list(String projectId, String collectionName) {
        try {
            Sort sort = Sort.by(Direction.DESC, CREATE_DATE_TIME.getFiled());
            DataCollection dataCollection = DataCollection.builder().projectId(projectId).collectionName(collectionName)
                .build();
            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher(PROJECT_ID.getFiled(), GenericPropertyMatchers.exact())
                .withMatcher(REMOVE.getFiled(), GenericPropertyMatchers.exact())
                .withStringMatcher(StringMatcher.CONTAINING);
            Example<DataCollection> example = Example.of(dataCollection, exampleMatcher);
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
            DataCollection dataCollection = dataCollectionMapper.toEntity(dataCollectionRequest);
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
            DataCollection dataCollection = dataCollectionMapper.toEntity(dataCollectionRequest);
            Optional<DataCollection> optional = dataCollectionRepository.findById(dataCollection.getId());
            if (optional.isEmpty()) {
                return Boolean.FALSE;
            }
            dataCollectionRepository.save(dataCollection);
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

}