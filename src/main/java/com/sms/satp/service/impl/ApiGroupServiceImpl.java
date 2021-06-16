package com.sms.satp.service.impl;

import static com.sms.satp.common.exception.ErrorCode.GET_API_GROUP_LIST_ERROR;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.response.ApiGroupResponse;
import com.sms.satp.entity.group.ApiGroupEntity;
import com.sms.satp.mapper.ApiGroupMapper;
import com.sms.satp.repository.ApiGroupRepository;
import com.sms.satp.service.ApiGroupService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApiGroupServiceImpl implements ApiGroupService {

    private final ApiGroupRepository apiGroupRepository;
    private final ApiGroupMapper apiGroupMapper;

    public ApiGroupServiceImpl(ApiGroupRepository apiGroupRepository, ApiGroupMapper apiGroupMapper) {
        this.apiGroupRepository = apiGroupRepository;
        this.apiGroupMapper = apiGroupMapper;
    }

    @Override
    public List<ApiGroupResponse> list(String projectId) {
        try {
            List<ApiGroupEntity> apiGroupEntityList = apiGroupRepository.findApiGroupEntitiesByProjectId(projectId);
            return apiGroupMapper.toResponse(apiGroupEntityList);
        } catch (Exception e) {
            log.error("Failed to list the ApiGroupService!", e);
            throw new ApiTestPlatformException(GET_API_GROUP_LIST_ERROR);
        }
    }

}
