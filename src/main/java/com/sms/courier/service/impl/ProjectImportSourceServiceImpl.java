package com.sms.courier.service.impl;

import com.sms.courier.dto.request.ProjectImportSourceRequest;
import com.sms.courier.dto.response.ProjectImportFlowResponse;
import com.sms.courier.dto.response.ProjectImportSourceResponse;
import com.sms.courier.entity.project.ProjectImportFlowEntity;
import com.sms.courier.entity.project.ProjectImportSourceEntity;
import com.sms.courier.mapper.ProjectImportSourceMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.ProjectImportFlowRepository;
import com.sms.courier.repository.ProjectImportSourceRepository;
import com.sms.courier.service.ProjectImportSourceService;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class ProjectImportSourceServiceImpl implements ProjectImportSourceService {

    private final ProjectImportSourceMapper projectImportSourceMapper;
    private final ProjectImportSourceRepository projectImportSourceRepository;
    private final ProjectImportFlowRepository projectImportFlowRepository;
    private final CommonRepository commonRepository;


    public ProjectImportSourceServiceImpl(ProjectImportSourceMapper projectImportSourceMapper,
        ProjectImportSourceRepository projectImportSourceRepository,
        ProjectImportFlowRepository projectImportFlowRepository,
        CommonRepository commonRepository) {
        this.projectImportSourceMapper = projectImportSourceMapper;
        this.projectImportSourceRepository = projectImportSourceRepository;
        this.projectImportFlowRepository = projectImportFlowRepository;
        this.commonRepository = commonRepository;
    }

    @Override
    public Boolean create(ProjectImportSourceRequest projectImportSourceRequest) {
        this.projectImportSourceRepository
            .insert(projectImportSourceMapper.toProjectImportSourceEntity(projectImportSourceRequest));
        return true;
    }

    @Override
    public Boolean update(ProjectImportSourceRequest projectImportSourceRequest) {

        this.projectImportSourceRepository
            .save(projectImportSourceMapper.toProjectImportSourceEntity(projectImportSourceRequest));
        return true;
    }

    @Override
    public ProjectImportSourceResponse findById(String id) {
        return this.projectImportSourceRepository.findById(id)
            .map(projectImportSourceMapper::toProjectImportSourceResponse).orElse(null);

    }

    @Override
    public List<ProjectImportSourceResponse> findByProjectId(String projectId) {
        List<ProjectImportSourceResponse> result = this.projectImportSourceRepository
            .findByProjectIdAndRemovedIsFalse(projectId);
        result.forEach((projectImportSourceResponse -> {
            ProjectImportFlowEntity projectImportFlowEntity = projectImportFlowRepository
                .findFirstByImportSourceIdOrderByCreateDateTimeDesc(projectImportSourceResponse.getId());
            projectImportSourceResponse.setImportStatus(
                Objects.nonNull(projectImportFlowEntity) ? projectImportFlowEntity.getImportStatus().getCode() : null
            );
        }));
        return result;
    }

    @Override
    public Boolean delete(List<String> ids) {
        return commonRepository.deleteByIds(ids, ProjectImportSourceEntity.class);
    }

    @Override
    public Iterable<ProjectImportSourceEntity> findByIds(List<String> proImpSourceIds) {
        return this.projectImportSourceRepository.findAllById(proImpSourceIds);
    }

    @Override
    public ProjectImportFlowResponse getProjectImportFlow(String projectId) {
        return projectImportFlowRepository.findFirstByProjectId(projectId);
    }
}
