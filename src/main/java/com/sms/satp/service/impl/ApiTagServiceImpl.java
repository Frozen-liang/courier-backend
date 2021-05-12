package com.sms.satp.service.impl;

import static com.sms.satp.common.constant.CommonFiled.CREATE_DATE_TIME;
import static com.sms.satp.common.constant.CommonFiled.ID;
import static com.sms.satp.common.constant.CommonFiled.PROJECT_ID;
import static com.sms.satp.common.exception.ErrorCode.ADD_API_TAG_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_API_TAG_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_API_TAG_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_TAG_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_TAG_LIST_ERROR;

import com.mongodb.client.result.DeleteResult;
import com.sms.satp.common.enums.ApiTagType;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.ApiTagRequest;
import com.sms.satp.dto.response.ApiTagResponse;
import com.sms.satp.entity.tag.ApiTag;
import com.sms.satp.mapper.ApiTagMapper;
import com.sms.satp.repository.ApiTagRepository;
import com.sms.satp.service.ApiTagService;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApiTagServiceImpl implements ApiTagService {

    private final ApiTagRepository apiTagRepository;
    private final ApiTagMapper apiTagMapper;
    private final MongoTemplate mongoTemplate;
    private static final String TAG_TYPE = "tagType";

    public ApiTagServiceImpl(ApiTagRepository apiTagRepository, ApiTagMapper apiTagMapper,
        MongoTemplate mongoTemplate) {
        this.apiTagRepository = apiTagRepository;
        this.apiTagMapper = apiTagMapper;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public ApiTagResponse findById(String id) {
        try {
            Optional<ApiTag> optional = apiTagRepository.findById(id);
            return apiTagMapper.toDto(optional.orElse(null));
        } catch (Exception e) {
            log.error("Failed to get the ApiTag by id!", e);
            throw new ApiTestPlatformException(GET_API_TAG_BY_ID_ERROR);
        }
    }

    @Override
    public List<ApiTagResponse> list(String projectId, String tagName, Integer tagType) {
        try {
            ApiTag apiTag = ApiTag.builder().projectId(projectId).tagName(tagName)
                .tagType(Objects.nonNull(tagType) ? ApiTagType.getType(tagType) : null).build();
            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher(PROJECT_ID, ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher(TAG_TYPE, ExampleMatcher.GenericPropertyMatchers.exact())
                .withStringMatcher(StringMatcher.CONTAINING)
                .withIgnoreNullValues();
            Example<ApiTag> example = Example.of(apiTag, exampleMatcher);
            Sort sort = Sort.by(Direction.DESC, CREATE_DATE_TIME);
            List<ApiTag> list = apiTagRepository.findAll(example, sort);
            return apiTagMapper.toDtoList(list);
        } catch (Exception e) {
            log.error("Failed to get the ApiTag list!", e);
            throw new ApiTestPlatformException(GET_API_TAG_LIST_ERROR);
        }
    }

    @Override
    public Boolean add(ApiTagRequest apiTagRequest) {
        log.info("ApiTagService-add()-params: [ApiTag]={}", apiTagRequest.toString());
        try {
            ApiTag apiTag = apiTagMapper.toEntity(apiTagRequest);
            apiTagRepository.insert(apiTag);
        } catch (Exception e) {
            log.error("Failed to add the ApiTag!", e);
            throw new ApiTestPlatformException(ADD_API_TAG_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean edit(ApiTagRequest apiTagRequest) {
        log.info("ApiTagService-edit()-params: [ApiTag]={}", apiTagRequest.toString());
        try {
            ApiTag apiTag = apiTagMapper.toEntity(apiTagRequest);
            Optional<ApiTag> optional = apiTagRepository.findById(apiTagRequest.getId());
            if (optional.isEmpty()) {
                return Boolean.FALSE;
            }
            optional.ifPresent((oldApiTag) -> {
                apiTag.setCreateUserId(oldApiTag.getCreateUserId());
                apiTag.setCreateDateTime(oldApiTag.getCreateDateTime());
                apiTagRepository.save(apiTag);
            });
        } catch (Exception e) {
            log.error("Failed to add the ApiTag!", e);
            throw new ApiTestPlatformException(EDIT_API_TAG_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean delete(String[] ids) {
        try {
            Query query = Query.query(Criteria.where(ID).in(Arrays.asList(ids)));
            DeleteResult deleteResult = mongoTemplate.remove(query, ApiTag.class);
            if (deleteResult.getDeletedCount() > 0) {
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            log.error("Failed to delete the ApiTag!", e);
            throw new ApiTestPlatformException(DELETE_API_TAG_BY_ID_ERROR);
        }
        return Boolean.FALSE;
    }

}
