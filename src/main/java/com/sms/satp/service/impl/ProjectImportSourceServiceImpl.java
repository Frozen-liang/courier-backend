package com.sms.satp.service.impl;

import com.sms.satp.dto.request.ProjectImportSourceRequest;
import com.sms.satp.dto.response.ProjectImportSourceResponse;
import com.sms.satp.entity.project.ProjectImportSourceEntity;
import com.sms.satp.mapper.ProjectImportSourceMapper;
import com.sms.satp.repository.CommonDeleteRepository;
import com.sms.satp.repository.ProjectImportSourceRepository;
import com.sms.satp.service.ProjectImportSourceService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProjectImportSourceServiceImpl implements ProjectImportSourceService {

    private final ProjectImportSourceMapper projectImportSourceMapper;
    private final ProjectImportSourceRepository projectImportSourceRepository;
    private final CommonDeleteRepository commonDeleteRepository;


    public ProjectImportSourceServiceImpl(ProjectImportSourceMapper projectImportSourceMapper,
        ProjectImportSourceRepository projectImportSourceRepository,
        CommonDeleteRepository commonDeleteRepository) {
        this.projectImportSourceMapper = projectImportSourceMapper;
        this.projectImportSourceRepository = projectImportSourceRepository;
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
        return this.projectImportSourceRepository.findByProjectIdAndRemovedIsFalse(projectId);
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
