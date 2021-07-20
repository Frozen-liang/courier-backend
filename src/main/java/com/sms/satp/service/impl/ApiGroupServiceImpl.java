package com.sms.satp.service.impl;

import static com.sms.satp.common.exception.ErrorCode.ADD_API_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_API_GROUP_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_API_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_GROUP_LIST_ERROR;
import static com.sms.satp.utils.Assert.isTrue;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.ApiGroupRequest;
import com.sms.satp.dto.response.ApiGroupResponse;
import com.sms.satp.entity.group.ApiGroupEntity;
import com.sms.satp.infrastructure.id.DefaultIdentifierGenerator;
import com.sms.satp.mapper.ApiGroupMapper;
import com.sms.satp.repository.ApiGroupRepository;
import com.sms.satp.repository.CustomizedApiRepository;
import com.sms.satp.service.ApiGroupService;
import com.sms.satp.utils.ExceptionUtils;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApiGroupServiceImpl implements ApiGroupService {

    private static final int MAX_DEPTH = 3;
    private final ApiGroupRepository apiGroupRepository;
    private final CustomizedApiRepository customizedApiRepository;
    private final ApiGroupMapper apiGroupMapper;
    private final DefaultIdentifierGenerator identifierGenerator = DefaultIdentifierGenerator.getSharedInstance();

    public ApiGroupServiceImpl(ApiGroupRepository apiGroupRepository,
        CustomizedApiRepository customizedApiRepository, ApiGroupMapper apiGroupMapper) {
        this.apiGroupRepository = apiGroupRepository;
        this.customizedApiRepository = customizedApiRepository;
        this.apiGroupMapper = apiGroupMapper;
    }

    @Override
    public List<ApiGroupResponse> list(String projectId, String groupId) {
        try {
            List<ApiGroupEntity> apiGroupEntityList;
            if (StringUtils.isBlank(groupId)) {
                apiGroupEntityList = apiGroupRepository.findByProjectIdAndDepth(projectId, 1);
            } else {
                apiGroupEntityList = apiGroupRepository.findByParentId(groupId);
            }
            return apiGroupMapper.toResponse(apiGroupEntityList);
        } catch (Exception e) {
            log.error("Failed to list the ApiGroupService!", e);
            throw new ApiTestPlatformException(GET_API_GROUP_LIST_ERROR);
        }
    }

    @Override
    public Boolean add(ApiGroupRequest request) {
        try {
            ApiGroupEntity apiGroupEntity = apiGroupMapper.toEntity(request);
            String parentId = apiGroupEntity.getParentId();
            ApiGroupEntity parentGroup = ApiGroupEntity.builder().depth(0).build();
            if (StringUtils.isNotBlank(parentId)) {
                parentGroup = apiGroupRepository.findById(parentId)
                    .orElseThrow(() -> ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "ApiGroup", parentId));
            }
            isTrue(parentGroup.getDepth() < MAX_DEPTH, "The group depth must be less than three.");
            Long realGroupId = identifierGenerator.nextId();
            parentGroup.getPath().add(realGroupId);
            apiGroupEntity
                .setDepth(parentGroup.getDepth() + 1);
            apiGroupEntity.setRealGroupId(realGroupId);
            apiGroupEntity.setPath(parentGroup.getPath());
            apiGroupRepository.insert(apiGroupEntity);
            return Boolean.TRUE;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Fail to add ApiGroup.");
            throw ExceptionUtils.mpe(ADD_API_GROUP_ERROR);
        }
    }

    @Override
    public Boolean edit(ApiGroupRequest request) {
        try {
            ApiGroupEntity apiGroupEntity = apiGroupMapper.toEntity(request);
            String id = apiGroupEntity.getId();
            ApiGroupEntity oldApiGroup = apiGroupRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "ApiGroup", id));
            apiGroupEntity.setRealGroupId(oldApiGroup.getRealGroupId());
            apiGroupEntity.setPath(oldApiGroup.getPath());
            apiGroupEntity.setDepth(oldApiGroup.getDepth());
            apiGroupEntity.setParentId(oldApiGroup.getParentId());
            apiGroupRepository.save(apiGroupEntity);
            return Boolean.TRUE;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Fail to add ApiGroup.");
            throw ExceptionUtils.mpe(EDIT_API_GROUP_ERROR);
        }
    }

    @Override
    public Boolean delete(String id) {
        try {
            ApiGroupEntity apiGroup = apiGroupRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "ApiGroup", id));
            //Query all apiGroup contain children groups
            List<String> ids = apiGroupRepository
                .findAllByPathContains(apiGroup.getRealGroupId()).map(ApiGroupEntity::getId)
                .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(ids)) {
                apiGroupRepository.deleteAllByIdIn(ids);
                customizedApiRepository.deleteByGroupIds(ids);
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Fail to delete ApiGroup.");
            throw ExceptionUtils.mpe(DELETE_API_GROUP_BY_ID_ERROR);
        }
    }

}
