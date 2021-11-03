package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.PROJECT_IMPORT_SOURCE;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.field.CommonField.CREATE_DATE_TIME;
import static com.sms.courier.common.field.CommonField.CREATE_USER_ID;
import static com.sms.courier.common.field.CommonField.ID;
import static com.sms.courier.common.field.CommonField.PROJECT_ID;
import static com.sms.courier.common.field.CommonField.USERNAME;
import static com.sms.courier.common.field.ProjectImportFlowField.IMPORT_SOURCE_ID;
import static com.sms.courier.common.field.ProjectImportFlowField.IMPORT_STATUS;

import com.google.common.collect.Lists;
import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.enums.CollectionName;
import com.sms.courier.dto.request.ProjectImportFlowPageRequest;
import com.sms.courier.dto.request.ProjectImportSourceRequest;
import com.sms.courier.dto.response.ProjectImportFlowResponse;
import com.sms.courier.dto.response.ProjectImportSourceResponse;
import com.sms.courier.entity.mongo.LookupField;
import com.sms.courier.entity.mongo.LookupVo;
import com.sms.courier.entity.mongo.QueryVo;
import com.sms.courier.entity.project.ProjectImportFlowEntity;
import com.sms.courier.entity.project.ProjectImportSourceEntity;
import com.sms.courier.mapper.ProjectImportSourceMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.ProjectImportFlowRepository;
import com.sms.courier.repository.ProjectImportSourceRepository;
import com.sms.courier.service.ProjectImportSourceService;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
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
    @LogRecord(operationType = ADD, operationModule = PROJECT_IMPORT_SOURCE,
        template = "{{#projectImportSourceRequest.name}}")
    public Boolean create(ProjectImportSourceRequest projectImportSourceRequest) {
        this.projectImportSourceRepository
            .insert(projectImportSourceMapper.toProjectImportSourceEntity(projectImportSourceRequest));
        return true;
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = PROJECT_IMPORT_SOURCE,
        template = "{{#projectImportSourceRequest.name}}")
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
    @LogRecord(operationType = DELETE, operationModule = PROJECT_IMPORT_SOURCE,
        template = "{{#result?.![#this.name]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
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

    @Override
    public Page<ProjectImportFlowResponse> pageProjectImportFlow(ProjectImportFlowPageRequest request) {
        QueryVo queryVo = QueryVo.builder().collectionName("ProjectImportFlow").lookupVo(Lists.newArrayList(
            LookupVo.builder()
                .from(CollectionName.USER)
                .localField(CREATE_USER_ID)
                .foreignField(ID)
                .as("user")
                .queryFields(Lists.newArrayList(LookupField.builder().field(USERNAME).alias("createUsername").build()))
                .build()))
            .criteriaList(List.of(PROJECT_ID.is(request.getProjectId()),
                CREATE_DATE_TIME.lteAndGte(request.getStartTime(), request.getEndTime()),
                IMPORT_SOURCE_ID.is(request.getImportSourceId()), IMPORT_STATUS.is(request.getImportStatus())))
            .build();
        return commonRepository.page(queryVo, request, ProjectImportFlowResponse.class);
    }
}
