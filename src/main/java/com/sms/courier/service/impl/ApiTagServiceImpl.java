package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.API_TAG;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.exception.ErrorCode.ADD_API_TAG_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_API_TAG_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_API_TAG_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_API_TAG_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_API_TAG_LIST_ERROR;
import static com.sms.courier.common.field.CommonField.CREATE_DATE_TIME;
import static com.sms.courier.common.field.CommonField.PROJECT_ID;
import static com.sms.courier.utils.Assert.isTrue;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.enums.ApiTagType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.field.ApiField;
import com.sms.courier.common.field.ApiTestCaseField;
import com.sms.courier.common.field.SceneField;
import com.sms.courier.dto.request.ApiTagListRequest;
import com.sms.courier.dto.request.ApiTagRequest;
import com.sms.courier.dto.response.ApiTagResponse;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import com.sms.courier.entity.tag.ApiTagEntity;
import com.sms.courier.mapper.ApiTagMapper;
import com.sms.courier.repository.ApiTagRepository;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.service.ApiTagService;
import com.sms.courier.utils.ExceptionUtils;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
    private final CommonRepository commonRepository;
    private final ApiTagMapper apiTagMapper;
    private static final String TAG_TYPE = "tagType";

    public ApiTagServiceImpl(ApiTagRepository apiTagRepository,
        CommonRepository commonRepository, ApiTagMapper apiTagMapper) {
        this.apiTagRepository = apiTagRepository;
        this.commonRepository = commonRepository;
        this.apiTagMapper = apiTagMapper;
    }

    @Override
    public ApiTagResponse findById(String id) {
        return apiTagRepository.findById(id).map(apiTagMapper::toDto)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_API_TAG_BY_ID_ERROR));
    }

    @Override
    public List<ApiTagResponse> list(ApiTagListRequest apiTagListRequest) {
        try {
            ApiTagEntity apiTag = apiTagMapper.listRequestToApiTag(apiTagListRequest);
            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher(PROJECT_ID.getName(), ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher(TAG_TYPE, ExampleMatcher.GenericPropertyMatchers.exact())
                .withStringMatcher(StringMatcher.CONTAINING)
                .withIgnoreNullValues();
            Example<ApiTagEntity> example = Example.of(apiTag, exampleMatcher);
            Sort sort = Sort.by(Direction.DESC, CREATE_DATE_TIME.getName());
            List<ApiTagEntity> list = apiTagRepository.findAll(example, sort);
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
            ApiTagEntity apiTag = apiTagMapper.toEntity(apiTagRequest);
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
            ApiTagEntity apiTag = apiTagMapper.toEntity(apiTagRequest);
            apiTagRepository.save(apiTag);
        } catch (ApiTestPlatformException courierException) {
            log.error(courierException.getMessage());
            throw courierException;
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
            Map<ApiTagType, List<ApiTagEntity>> map = apiTagRepository.findAllByIdIn(ids)
                .collect(Collectors.groupingBy(ApiTagEntity::getTagType));
            map.forEach((key, value) -> {
                switch (key) {
                    case API:
                        commonRepository.removeTags(ApiField.TAG_ID,
                            value.stream().map(ApiTagEntity::getId).collect(Collectors.toList()), ApiEntity.class);
                        break;
                    case CASE:
                        commonRepository.removeTags(ApiTestCaseField.TAG_IDS,
                            value.stream().map(ApiTagEntity::getId).collect(Collectors.toList()),
                            ApiTestCaseEntity.class);
                        break;
                    case SCENE:
                        commonRepository.removeTags(SceneField.TAG_ID,
                            value.stream().map(ApiTagEntity::getId).collect(Collectors.toList()),
                            SceneCaseApiEntity.class);
                        break;
                    default:
                        break;
                }
            });
            Long removeCount = apiTagRepository.deleteAllByIdIsIn(ids);
            return removeCount > 0;
        } catch (Exception e) {
            log.error("Failed to delete the ApiTag!", e);
            throw new ApiTestPlatformException(DELETE_API_TAG_BY_ID_ERROR);
        }
    }

}
