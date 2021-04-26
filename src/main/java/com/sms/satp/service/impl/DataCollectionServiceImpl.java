package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.ADD_DATA_COLLECTION_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_DATA_COLLECTION_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_DATA_COLLECTION_ERROR;
import static com.sms.satp.common.ErrorCode.GET_DATA_COLLECTION_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_DATA_COLLECTION_LIST_ERROR;
import static com.sms.satp.common.ErrorCode.GET_DATA_COLLECTION_PARAM_LIST_BY_ID_ERROR;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.datacollection.DataCollection;
import com.sms.satp.entity.dto.DataCollectionDto;
import com.sms.satp.mapper.DataCollectionMapper;
import com.sms.satp.repository.DataCollectionRepository;
import com.sms.satp.service.DataCollectionService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DataCollectionServiceImpl implements DataCollectionService {

    private final DataCollectionRepository dataCollectionRepository;
    private final DataCollectionMapper dataCollectionMapper;
    private final MongoTemplate mongoTemplate;
    private static final String PROJECT_ID = "projectId";
    private static final String CREATE_DATE_TIME = "createDataTime";
    private static final String REMOVE = "remove";
    private static final String PARAM_LIST = "paramList";
    private static final String ID = "id";

    public DataCollectionServiceImpl(DataCollectionRepository dataCollectionRepository,
        DataCollectionMapper dataCollectionMapper, MongoTemplate mongoTemplate) {
        this.dataCollectionRepository = dataCollectionRepository;
        this.dataCollectionMapper = dataCollectionMapper;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public DataCollectionDto findById(String id) {
        try {
            Optional<DataCollection> optional = dataCollectionRepository.findById(id);
            return dataCollectionMapper.toDto(optional.orElse(null));
        } catch (Exception e) {
            log.error("Failed to get the DataCollection by id!", e);
            throw new ApiTestPlatformException(GET_DATA_COLLECTION_BY_ID_ERROR);
        }
    }

    @Override
    public List<DataCollectionDto> list(String projectId, String collectionName) {
        try {
            Sort sort = Sort.by(Direction.DESC, CREATE_DATE_TIME);
            DataCollection dataCollection = DataCollection.builder().projectId(projectId).collectionName(collectionName)
                .build();
            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher(PROJECT_ID, GenericPropertyMatchers.exact())
                .withMatcher(REMOVE, GenericPropertyMatchers.exact())
                .withStringMatcher(StringMatcher.CONTAINING);
            Example<DataCollection> example = Example.of(dataCollection, exampleMatcher);
            return dataCollectionRepository.findAll(example, sort).stream().map(dataCollectionMapper::toDto).collect(
                Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to get the DataCollection list!", e);
            throw new ApiTestPlatformException(GET_DATA_COLLECTION_LIST_ERROR);
        }
    }


    @Override
    public void add(DataCollectionDto dataCollectionDto) {
        log.info("DataCollectionService-add()-params: [DataCollection]={}", dataCollectionDto.toString());
        try {
            DataCollection dataCollection = dataCollectionMapper.toEntity(dataCollectionDto);
            dataCollectionRepository.insert(dataCollection);
        } catch (Exception e) {
            log.error("Failed to add the DataCollection!", e);
            throw new ApiTestPlatformException(ADD_DATA_COLLECTION_ERROR);
        }
    }

    @Override
    public void edit(DataCollectionDto dataCollectionDto) {
        log.info("DataCollectionService-edit()-params: [DataCollection]={}", dataCollectionDto.toString());
        try {
            DataCollection dataCollection = dataCollectionMapper.toEntity(dataCollectionDto);
            dataCollectionRepository.findById(dataCollection.getId())
                .ifPresent((oldDataCollection) -> {
                    dataCollection.setCreateUserId(oldDataCollection.getCreateUserId());
                    dataCollection.setCreateDateTime(oldDataCollection.getCreateDateTime());
                    dataCollectionRepository.save(dataCollection);
                });
        } catch (Exception e) {
            log.error("Failed to add the DataCollection!", e);
            throw new ApiTestPlatformException(EDIT_DATA_COLLECTION_ERROR);
        }
    }

    @Override
    public void delete(String[] ids) {
        try {
            Iterable<DataCollection> dataCollections = dataCollectionRepository.findAllById(Arrays.asList(ids));
            dataCollections.forEach(dataCollection -> dataCollection.setRemove(Boolean.TRUE));
            dataCollectionRepository.saveAll(dataCollections);
        } catch (Exception e) {
            log.error("Failed to delete the DataCollection!", e);
            throw new ApiTestPlatformException(DELETE_DATA_COLLECTION_BY_ID_ERROR);
        }
    }

    @Override
    public List<String> getParamListById(String id) {
        try {
            Query query = new Query(Criteria.where(ID).is(id));
            query.fields().include(PARAM_LIST);
            DataCollection dataCollection = mongoTemplate.findOne(query, DataCollection.class);
            if (Objects.nonNull(dataCollection)) {
                return dataCollection.getParamList();
            }
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("Failed to get the DataCollectionParamList by Id!", e);
            throw new ApiTestPlatformException(GET_DATA_COLLECTION_PARAM_LIST_BY_ID_ERROR);
        }
    }

}