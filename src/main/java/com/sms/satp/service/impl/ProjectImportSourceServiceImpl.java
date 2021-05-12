package com.sms.satp.service.impl;

import com.sms.satp.dto.request.ProjectImportSourceRequest;
import com.sms.satp.dto.response.ProjectImportSourceResponse;
import com.sms.satp.entity.project.ProjectImportSourceEntity;
import com.sms.satp.mapper.ProjectImportSourceMapper;
import com.sms.satp.repository.ProjectImportSourceRepository;
import com.sms.satp.service.ProjectImportSourceService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProjectImportSourceServiceImpl implements ProjectImportSourceService {

    private final ProjectImportSourceMapper projectImportSourceMapper;
    private final ProjectImportSourceRepository projectImportSourceRepository;


    public ProjectImportSourceServiceImpl(ProjectImportSourceMapper projectImportSourceMapper,
        ProjectImportSourceRepository projectImportSourceRepository) {
        this.projectImportSourceMapper = projectImportSourceMapper;
        this.projectImportSourceRepository = projectImportSourceRepository;
    }

    @Override
    public Boolean create(ProjectImportSourceRequest projectImportSourceRequest) {
        this.projectImportSourceRepository
            .insert(projectImportSourceMapper.toProjectImportSourceEntity(projectImportSourceRequest,
                Optional.empty()));
        return true;
    }

    @Override
    public Boolean update(ProjectImportSourceRequest projectImportSourceRequest) {
        Optional<ProjectImportSourceEntity> oldSourceEntity = this.projectImportSourceRepository
            .findById(projectImportSourceRequest.getId());
        this.projectImportSourceRepository
            .save(projectImportSourceMapper.toProjectImportSourceEntity(projectImportSourceRequest,
                oldSourceEntity));
        return true;
    }

    @Override
    public ProjectImportSourceResponse findById(String id) {

        return this.projectImportSourceRepository.findById(id)
            .map(projectImportSourceMapper::toProjectImportSourceResponse).orElse(null);

    }

    @Override
    public List<ProjectImportSourceResponse> findByProjectId(String projectId) {
        return this.projectImportSourceRepository.findByProjectId(projectId);
    }
}
