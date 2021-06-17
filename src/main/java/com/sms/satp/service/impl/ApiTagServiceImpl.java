package com.sms.satp.service.impl;

import static com.sms.satp.common.enums.OperationModule.API_TAG;
import static com.sms.satp.common.enums.OperationType.ADD;
import static com.sms.satp.common.enums.OperationType.DELETE;
import static com.sms.satp.common.enums.OperationType.EDIT;
import static com.sms.satp.common.exception.ErrorCode.ADD_API_TAG_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_API_TAG_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_API_TAG_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_TAG_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_TAG_LIST_ERROR;
import static com.sms.satp.common.field.CommonFiled.CREATE_DATE_TIME;
import static com.sms.satp.common.field.CommonFiled.PROJECT_ID;
import static com.sms.satp.utils.Assert.isTrue;

import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.ApiTagRequest;
import com.sms.satp.dto.response.ApiTagResponse;
import com.sms.satp.entity.tag.ApiTag;
import com.sms.satp.mapper.ApiTagMapper;
import com.sms.satp.repository.ApiTagRepository;
import com.sms.satp.service.ApiTagService;
import com.sms.satp.utils.ExceptionUtils;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApiTagServiceImpl implements ApiTagService {

    private final ApiTagRepository apiTagRepository;
    private final ApiTagMapper apiTagMapper;
    private static final String TAG_TYPE = "tagType";

    public ApiTagServiceImpl(ApiTagRepository apiTagRepository, ApiTagMapper apiTagMapper) {
        this.apiTagRepository = apiTagRepository;
        this.apiTagMapper = apiTagMapper;
    }

    @Override
    public ApiTagResponse findById(String id) {
        return apiTagRepository.findById(id).map(apiTagMapper::toDto)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_API_TAG_BY_ID_ERROR));
    }

    @Override
    public List<ApiTagResponse> list(String projectId, String tagName) {
        try {
            ApiTag apiTag = ApiTag.builder().projectId(projectId).tagName(tagName).build();
            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher(PROJECT_ID.getFiled(), ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher(TAG_TYPE, ExampleMatcher.GenericPropertyMatchers.exact())
                .withStringMatcher(StringMatcher.CONTAINING)
                .withIgnoreNullValues();
            Example<ApiTag> example = Example.of(apiTag, exampleMatcher);
            Sort sort = Sort.by(Direction.DESC, CREATE_DATE_TIME.getFiled());
            List<ApiTag> list = apiTagRepository.findAll(example, sort);
            return apiTagMapper.toDtoList(list);
        } catch (Exception e) {
            log.error("Failed to get the ApiTag list!", e);
            throw new ApiTestPlatformException(GET_API_TAG_LIST_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = API_TAG, template = "{{#apiTagRequest.tagName}}")
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
    @LogRecord(operationType = EDIT, operationModule = API_TAG, template = "{{#apiTagRequest.tagName}}")
    public Boolean edit(ApiTagRequest apiTagRequest) {
        log.info("ApiTagService-edit()-params: [ApiTag]={}", apiTagRequest.toString());
        try {
            boolean exists = apiTagRepository.existsById(apiTagRequest.getId());
            isTrue(exists, EDIT_NOT_EXIST_ERROR, "ApiTag", apiTagRequest.getId());
            ApiTag apiTag = apiTagMapper.toEntity(apiTagRequest);
            apiTagRepository.save(apiTag);
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (Exception e) {
            log.error("Failed to edit the ApiTag!", e);
            throw ExceptionUtils.mpe(EDIT_API_TAG_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = API_TAG, template = "{{#result?.![#this.tagName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean delete(List<String> ids) {
        try {
            Long removeCount = apiTagRepository.deleteAllByIdIsIn(ids);
            return removeCount > 0;
        } catch (Exception e) {
            log.error("Failed to delete the ApiTag!", e);
            throw new ApiTestPlatformException(DELETE_API_TAG_BY_ID_ERROR);
        }
    }

}
