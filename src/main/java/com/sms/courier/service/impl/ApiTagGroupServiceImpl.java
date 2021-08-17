package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.API_TAG_GROUP;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.exception.ErrorCode.ADD_API_TAG_GROUP_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_API_TAG_GROUP_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_API_TAG_GROUP_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_API_TAG_GROUP_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_API_TAG_GROUP_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_API_TAG_GROUP_NAME_EXIST_ERROR;
import static com.sms.courier.utils.Assert.isFalse;
import static com.sms.courier.utils.Assert.isTrue;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.ApiTagGroupRequest;
import com.sms.courier.dto.response.ApiTagGroupResponse;
import com.sms.courier.entity.group.ApiTagGroupEntity;
import com.sms.courier.mapper.ApiTagGroupMapper;
import com.sms.courier.repository.ApiTagGroupRepository;
import com.sms.courier.service.ApiTagGroupService;
import com.sms.courier.utils.ExceptionUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApiTagGroupServiceImpl implements ApiTagGroupService {

    private final ApiTagGroupRepository apiTagGroupRepository;
    private final ApiTagGroupMapper apiTagGroupMapper;

    public ApiTagGroupServiceImpl(ApiTagGroupRepository apiTagGroupRepository,
        ApiTagGroupMapper apiTagGroupMapper) {
        this.apiTagGroupRepository = apiTagGroupRepository;
        this.apiTagGroupMapper = apiTagGroupMapper;
    }

    @Override
    public ApiTagGroupResponse findById(String id) {
        return apiTagGroupRepository.findById(id).map(apiTagGroupMapper::toDto)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_API_TAG_GROUP_BY_ID_ERROR));
    }

    @Override
    public List<ApiTagGroupResponse> list(String projectId) {
        try {
            return apiTagGroupMapper.toDtoList(apiTagGroupRepository.findByProjectId(projectId));
        } catch (Exception e) {
            log.error("Failed to get the ApiTagGroup list!", e);
            throw new ApiTestPlatformException(GET_API_TAG_GROUP_LIST_ERROR);
        }
    }


    @Override
    @LogRecord(operationType = ADD, operationModule = API_TAG_GROUP, template = "{{#apiTagGroupRequest.name}}")
    public Boolean add(ApiTagGroupRequest apiTagGroupRequest) {
        log.info("ApiTagGroupService-add()-params: [ApiTagGroup]={}", apiTagGroupRequest.toString());
        try {
            boolean exists = apiTagGroupRepository.existsByProjectIdAndName(apiTagGroupRequest.getProjectId(),
                apiTagGroupRequest.getName());
            isFalse(exists, THE_API_TAG_GROUP_NAME_EXIST_ERROR, apiTagGroupRequest.getName());
            ApiTagGroupEntity apiTagGroup = apiTagGroupMapper.toEntity(apiTagGroupRequest);
            apiTagGroupRepository.insert(apiTagGroup);
        } catch (ApiTestPlatformException courierException) {
            log.error(courierException.getMessage());
            throw courierException;
        } catch (Exception e) {
            log.error("Failed to add the ApiTagGroup!", e);
            throw new ApiTestPlatformException(ADD_API_TAG_GROUP_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = API_TAG_GROUP, template = "{{#apiTagGroupRequest.name}}")
    public Boolean edit(ApiTagGroupRequest apiTagGroupRequest) {
        log.info("ApiTagGroupService-edit()-params: [ApiTagGroup]={}", apiTagGroupRequest.toString());
        try {
            boolean exists = apiTagGroupRepository.existsById(apiTagGroupRequest.getId());
            isTrue(exists, EDIT_NOT_EXIST_ERROR, "ApiTagGroup", apiTagGroupRequest.getId());
            exists = apiTagGroupRepository.existsByProjectIdAndName(apiTagGroupRequest.getProjectId(),
                apiTagGroupRequest.getName());
            isFalse(exists, THE_API_TAG_GROUP_NAME_EXIST_ERROR, apiTagGroupRequest.getName());
            ApiTagGroupEntity apiTagGroup = apiTagGroupMapper.toEntity(apiTagGroupRequest);
            apiTagGroupRepository.save(apiTagGroup);
        } catch (ApiTestPlatformException courierException) {
            log.error(courierException.getMessage());
            throw courierException;
        } catch (Exception e) {
            log.error("Failed to add the ApiTagGroup!", e);
            throw new ApiTestPlatformException(EDIT_API_TAG_GROUP_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = API_TAG_GROUP,
        template = "{{#result?.![#this.name]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean delete(List<String> ids) {
        try {
            return apiTagGroupRepository.deleteByIdIn(ids) > 0;
        } catch (Exception e) {
            log.error("Failed to delete the ApiTagGroup!", e);
            throw new ApiTestPlatformException(DELETE_API_TAG_GROUP_BY_ID_ERROR);
        }
    }

}