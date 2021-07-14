package com.sms.satp.service.impl;

import com.sms.satp.dto.request.ProjectImportSourceRequest;
import com.sms.satp.dto.response.ProjectImportSourceResponse;
import com.sms.satp.entity.project.ProjectImportFlowEntity;
import com.sms.satp.entity.project.ProjectImportSourceEntity;
import com.sms.satp.mapper.ProjectImportSourceMapper;
import com.sms.satp.repository.CommonDeleteRepository;
import com.sms.satp.repository.ProjectImportFlowRepository;
import com.sms.satp.repository.ProjectImportSourceRepository;
import com.sms.satp.service.ProjectImportSourceService;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class ProjectImportSourceServiceImpl implements ProjectImportSourceService {

    private final ProjectImportSourceMapper projectImportSourceMapper;
    private final ProjectImportSourceRepository projectImportSourceRepository;
    private final ProjectImportFlowRepository projectImportFlowRepository;
    private final CommonDeleteRepository commonDeleteRepository;


    public ProjectImportSourceServiceImpl(ProjectImportSourceMapper projectImportSourceMapper,
        ProjectImportSourceRepository projectImportSourceRepository,
        ProjectImportFlowRepository projectImportFlowRepository,
        CommonDeleteRepository commonDeleteRepository) {
        this.projectImportSourceMapper = projectImportSourceMapper;
        this.projectImportSourceRepository = projectImportSourceRepository;
        this.projectImportFlowRepository = projectImportFlowRepository;
        this.commonDeleteRepository = commonDeleteRepository;
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
                .findFirstByImportSourceId(projectImportSourceResponse.getId());
            projectImportSourceResponse.setImportStatus(
                Objects.nonNull(projectImportFlowEntity) ? projectImportFlowEntity.getImportStatus().getCode() : null
            );
        }));
        return result;
    }

    @Override
    public Boolean delete(List<String> ids) {
        return commonDeleteRepository.deleteByIds(ids, ProjectImportSourceEntity.class);
    }

    @Override
    public Iterable<ProjectImportSourceEntity> findByIds(List<String> proImpSourceIds) {
        return this.projectImportSourceRepository.findAllById(proImpSourceIds);
    }
}
