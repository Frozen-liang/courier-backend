package com.sms.satp.service.impl;

import static com.sms.satp.common.constant.CommonFiled.CREATE_DATE_TIME;
import static com.sms.satp.common.constant.CommonFiled.ID;
import static com.sms.satp.common.constant.CommonFiled.MODIFY_DATE_TIME;
import static com.sms.satp.common.constant.CommonFiled.PROJECT_ID;
import static com.sms.satp.common.constant.CommonFiled.REMOVE;
import static com.sms.satp.common.exception.ErrorCode.ADD_DATA_COLLECTION_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_DATA_COLLECTION_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_DATA_COLLECTION_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_DATA_COLLECTION_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_DATA_COLLECTION_LIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_DATA_COLLECTION_PARAM_LIST_BY_ID_ERROR;

import com.mongodb.client.result.UpdateResult;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.DataCollectionRequest;
import com.sms.satp.dto.response.DataCollectionResponse;
import com.sms.satp.entity.datacollection.DataCollection;
import com.sms.satp.mapper.DataCollectionMapper;
import com.sms.satp.repository.DataCollectionRepository;
import com.sms.satp.service.DataCollectionService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DataCollectionServiceImpl implements DataCollectionService {

    private final DataCollectionRepository dataCollectionRepository;
    private final DataCollectionMapper dataCollectionMapper;
    private final MongoTemplate mongoTemplate;
    private static final String PARAM_LIST = "paramList";

    public DataCollectionServiceImpl(DataCollectionRepository dataCollectionRepository,
        DataCollectionMapper dataCollectionMapper, MongoTemplate mongoTemplate) {
        this.dataCollectionRepository = dataCollectionRepository;
        this.dataCollectionMapper = dataCollectionMapper;
        this.mongoTemplate = mongoTemplate;
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
            Sort sort = Sort.by(Direction.DESC, CREATE_DATE_TIME);
            DataCollection dataCollection = DataCollection.builder().projectId(projectId).collectionName(collectionName)
                .build();
            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher(PROJECT_ID, GenericPropertyMatchers.exact())
                .withMatcher(REMOVE, GenericPropertyMatchers.exact())
                .withStringMatcher(StringMatcher.CONTAINING);
            Example<DataCollection> example = Example.of(dataCollection, exampleMatcher);
            return dataCollectionMapper.toDtoList(dataCollectionRepository.findAll(example, sort));
        } catch (Exception e) {
            log.error("Failed to get the DataCollection list!", e);
            throw new ApiTestPlatformException(GET_DATA_COLLECTION_LIST_ERROR);
        }
    }

    @Override
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
    public Boolean edit(DataCollectionRequest dataCollectionRequest) {
        log.info("DataCollectionService-edit()-params: [DataCollection]={}", dataCollectionRequest.toString());
        try {
            DataCollection dataCollection = dataCollectionMapper.toEntity(dataCollectionRequest);
            Optional<DataCollection> optional = dataCollectionRepository.findById(dataCollection.getId());
            if (optional.isEmpty()) {
                return Boolean.FALSE;
            }
            optional.ifPresent((oldDataCollection) -> {
                dataCollection.setCreateUserId(oldDataCollection.getCreateUserId());
                dataCollection.setCreateDateTime(oldDataCollection.getCreateDateTime());
                dataCollectionRepository.save(dataCollection);
            });
        } catch (Exception e) {
            log.error("Failed to add the DataCollection!", e);
            throw new ApiTestPlatformException(EDIT_DATA_COLLECTION_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean delete(String[] ids) {
        try {
            Query query = new Query(Criteria.where(ID).in(Arrays.asList(ids)));
            Update update = Update.update(REMOVE, Boolean.TRUE);
            update.set(MODIFY_DATE_TIME, LocalDateTime.now());
            UpdateResult updateResult = mongoTemplate.updateMulti(query, update, DataCollection.class);
            if (updateResult.getModifiedCount() > 0) {
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            log.error("Failed to delete the DataCollection!", e);
            throw new ApiTestPlatformException(DELETE_DATA_COLLECTION_BY_ID_ERROR);
        }
        return Boolean.FALSE;
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