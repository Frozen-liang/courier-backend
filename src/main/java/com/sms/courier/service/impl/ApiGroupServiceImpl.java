package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.API_GROUP;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.exception.ErrorCode.ADD_API_GROUP_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_API_GROUP_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_API_GROUP_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_API_GROUP_LIST_ERROR;
import static com.sms.courier.utils.Assert.isTrue;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.ApiGroupRequest;
import com.sms.courier.dto.response.TreeResponse;
import com.sms.courier.entity.group.ApiGroupEntity;
import com.sms.courier.infrastructure.id.DefaultIdentifierGenerator;
import com.sms.courier.mapper.ApiGroupMapper;
import com.sms.courier.repository.ApiGroupRepository;
import com.sms.courier.repository.CustomizedApiRepository;
import com.sms.courier.service.ApiGroupService;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.TreeUtils;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApiGroupServiceImpl implements ApiGroupService {

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
    public List<TreeResponse> list(String projectId) {
        try {
            List<ApiGroupEntity> apiGroupEntities = apiGroupRepository
                .findByProjectIdOrderByNameAscCreateDateTimeDesc(projectId);
            return TreeUtils.createTree(apiGroupMapper.toResponse(apiGroupEntities));
        } catch (Exception e) {
            log.error("Failed to list the ApiGroupService!", e);
            throw ExceptionUtils.mpe(GET_API_GROUP_LIST_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = API_GROUP, template = "{{#request.name}}")
    public Boolean add(ApiGroupRequest request) {
        try {
            ApiGroupEntity apiGroupEntity = apiGroupMapper.toEntity(request);
            String parentId = apiGroupEntity.getParentId();
            ApiGroupEntity parentGroup = ApiGroupEntity.builder().depth(0).build();
            if (StringUtils.isNotBlank(parentId)) {
                parentGroup = apiGroupRepository.findById(parentId)
                    .orElseThrow(() -> ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "ApiGroup", parentId));
            }
            isTrue(parentGroup.getDepth() < Constants.MAX_DEPTH, "The group depth must be less than three.");
            Long realGroupId = identifierGenerator.nextId();
            parentGroup.getPath().add(realGroupId);
            apiGroupEntity.setDepth(parentGroup.getDepth() + 1);
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
    @LogRecord(operationType = EDIT, operationModule = API_GROUP, template = "{{#request.name}}")
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
    @LogRecord(operationType = DELETE, operationModule = API_GROUP, template = "{{#result?.name}}",
        enhance = @Enhance(enable = true))
    public Boolean delete(String id) {
        try {
            ApiGroupEntity apiGroup = apiGroupRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "ApiGroup", id));
            List<String> ids = Objects.nonNull(apiGroup.getRealGroupId())
                ? apiGroupRepository.findAllByPathContains(apiGroup.getRealGroupId())
                .map(ApiGroupEntity::getId).collect(Collectors.toList())
                : List.of(id);

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
