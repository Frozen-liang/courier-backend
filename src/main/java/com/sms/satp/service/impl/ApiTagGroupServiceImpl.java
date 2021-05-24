package com.sms.satp.service.impl;

import static com.sms.satp.common.exception.ErrorCode.ADD_API_TAG_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_API_TAG_GROUP_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_API_TAG_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_TAG_GROUP_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_TAG_GROUP_LIST_ERROR;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.ApiTagGroupRequest;
import com.sms.satp.dto.response.ApiTagGroupResponse;
import com.sms.satp.entity.group.ApiTagGroup;
import com.sms.satp.mapper.ApiTagGroupMapper;
import com.sms.satp.repository.ApiTagGroupRepository;
import com.sms.satp.service.ApiTagGroupService;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApiTagGroupServiceImpl implements ApiTagGroupService {

    private final ApiTagGroupRepository apiTagGroupRepository;
    private final ApiTagGroupMapper apiTagGroupMapper;
    private final MongoTemplate mongoTemplate;

    public ApiTagGroupServiceImpl(ApiTagGroupRepository apiTagGroupRepository,
        ApiTagGroupMapper apiTagGroupMapper,
        MongoTemplate mongoTemplate) {
        this.apiTagGroupRepository = apiTagGroupRepository;
        this.apiTagGroupMapper = apiTagGroupMapper;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public ApiTagGroupResponse findById(String id) {
        try {
            Optional<ApiTagGroup> optional = apiTagGroupRepository.findById(id);
            return apiTagGroupMapper.toDto(optional.orElse(null));
        } catch (Exception e) {
            log.error("Failed to get the ApiTagGroup by id!", e);
            throw new ApiTestPlatformException(GET_API_TAG_GROUP_BY_ID_ERROR);
        }
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
    public Boolean add(ApiTagGroupRequest apiTagGroupRequest) {
        log.info("ApiTagGroupService-add()-params: [ApiTagGroup]={}", apiTagGroupRequest.toString());
        try {
            ApiTagGroup apiTagGroup = apiTagGroupMapper.toEntity(apiTagGroupRequest);
            Optional<ApiTagGroup> optional = apiTagGroupRepository
                .findByProjectIdAndName(apiTagGroup.getProjectId(), apiTagGroup.getName());
            if (optional.isPresent()) {
                return Boolean.FALSE;
            }
            apiTagGroupRepository.insert(apiTagGroup);
        } catch (Exception e) {
            log.error("Failed to add the ApiTagGroup!", e);
            throw new ApiTestPlatformException(ADD_API_TAG_GROUP_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean edit(ApiTagGroupRequest apiTagGroupRequest) {
        log.info("ApiTagGroupService-edit()-params: [ApiTagGroup]={}", apiTagGroupRequest.toString());
        try {
            ApiTagGroup apiTagGroup = apiTagGroupMapper.toEntity(apiTagGroupRequest);
            Optional<ApiTagGroup> optional = apiTagGroupRepository.findById(apiTagGroup.getId());
            if (optional.isEmpty()) {
                return Boolean.FALSE;
            }
            apiTagGroupRepository.save(apiTagGroup);
        } catch (Exception e) {
            log.error("Failed to add the ApiTagGroup!", e);
            throw new ApiTestPlatformException(EDIT_API_TAG_GROUP_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean delete(List<String> ids) {
        try {
            return apiTagGroupRepository.deleteByIdIn(ids) > 0;
        } catch (Exception e) {
            log.error("Failed to delete the ApiTagGroup!", e);
            throw new ApiTestPlatformException(DELETE_API_TAG_GROUP_BY_ID_ERROR);
        }
    }

}