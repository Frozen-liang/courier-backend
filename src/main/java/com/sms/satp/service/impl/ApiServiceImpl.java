package com.sms.satp.service.impl;

import static com.sms.satp.common.exception.ErrorCode.ADD_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_API_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_PAGE_ERROR;

import com.sms.satp.common.enums.DocumentType;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.ApiImportRequest;
import com.sms.satp.dto.request.ApiPageRequest;
import com.sms.satp.dto.request.ApiRequest;
import com.sms.satp.dto.response.ApiResponse;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.api.ApiHistoryEntity;
import com.sms.satp.entity.group.ApiGroupEntity;
import com.sms.satp.entity.project.ProjectImportFlowEntity;
import com.sms.satp.mapper.ApiHistoryMapper;
import com.sms.satp.mapper.ApiMapper;
import com.sms.satp.parser.ApiDocumentChecker;
import com.sms.satp.parser.ApiDocumentTransformer;
import com.sms.satp.parser.DocumentReader;
import com.sms.satp.parser.common.DocumentDefinition;
import com.sms.satp.repository.ApiGroupRepository;
import com.sms.satp.repository.ApiHistoryRepository;
import com.sms.satp.repository.ApiRepository;
import com.sms.satp.repository.CustomizedApiRepository;
import com.sms.satp.repository.ProjectEntityRepository;
import com.sms.satp.repository.ProjectImportFlowRepository;
import com.sms.satp.service.ApiService;
import com.sms.satp.utils.ExceptionUtils;
import com.sms.satp.utils.PageDtoConverter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class ApiServiceImpl implements ApiService, ApplicationContextAware {

    private final ProjectEntityRepository projectEntityRepository;
    private final ApiRepository apiRepository;
    private final ApiHistoryRepository apiHistoryRepository;
    private final ApiMapper apiMapper;
    private final ApiHistoryMapper apiHistoryMapper;
    private final CustomizedApiRepository customizedApiRepository;
    private final ApiGroupRepository apiGroupRepository;
    private final ProjectImportFlowRepository projectImportFlowRepository;
    private ApplicationContext applicationContext;

    public ApiServiceImpl(ProjectEntityRepository projectEntityRepository,
        ApiRepository apiRepository, ApiHistoryRepository apiHistoryRepository, ApiMapper apiMapper,
        ApiHistoryMapper apiHistoryMapper, CustomizedApiRepository customizedApiRepository,
        ApiGroupRepository apiGroupRepository,
        ProjectImportFlowRepository projectImportFlowRepository) {
        this.projectEntityRepository = projectEntityRepository;
        this.apiRepository = apiRepository;
        this.apiHistoryRepository = apiHistoryRepository;
        this.apiMapper = apiMapper;
        this.apiHistoryMapper = apiHistoryMapper;
        this.customizedApiRepository = customizedApiRepository;
        this.apiGroupRepository = apiGroupRepository;
        this.projectImportFlowRepository = projectImportFlowRepository;
    }

    @Override
    public boolean importDocument(ApiImportRequest apiImportRequest) {
        final ProjectImportFlowEntity projectImportFlowEntity = projectImportFlowRepository.save(
            ProjectImportFlowEntity.builder().projectId(apiImportRequest.getProjectId()).startTime(LocalDateTime.now())
                .build());
        DocumentType documentType = DocumentType.getType(apiImportRequest.getDocumentType());
        DocumentDefinition definition = parserDocument(apiImportRequest);
        ApiDocumentTransformer<?> transformer = documentType.getTransformer();
        Set<ApiGroupEntity> apiGroupEntities = transformer.toApiGroupEntities(definition);
        apiGroupEntities.forEach(apiGroup -> apiGroup.setProjectId(apiImportRequest.getProjectId()));
        List<ApiGroupEntity> oldGroupEntities =
            apiGroupRepository.findApiGroupEntitiesByProjectId(apiImportRequest.getProjectId());
        Collection<ApiGroupEntity> unsavedGroupEntities = CollectionUtils
            .subtract(apiGroupEntities, oldGroupEntities);
        List<ApiGroupEntity> newApiGroupEntities = apiGroupRepository.saveAll(unsavedGroupEntities);
        newApiGroupEntities.addAll(oldGroupEntities);

        List<ApiEntity> apiEntities = transformer.toApiEntities(definition);
        apiEntities.forEach(apiEntity -> apiEntity.setProjectId(apiImportRequest.getProjectId()));
        List<ApiDocumentChecker> apiDocumentCheckers = documentType.getApiDocumentCheckers();
        boolean allPass = apiDocumentCheckers.stream()
            .allMatch(apiDocumentChecker -> apiDocumentChecker
                .check(apiEntities, projectImportFlowEntity, this.applicationContext));
        if (allPass) {

            Function<ApiEntity, String> generateUniqueId =
                apiEntity -> apiEntity.getSwaggerId().concat(apiEntity.getRequestMethod().name());
            Map<String, ApiEntity> oldApiEntities = apiRepository
                .findApiEntitiesByProjectIdAndSwaggerIdNotNull(apiImportRequest.getProjectId()).stream()
                .collect(Collectors.toMap(generateUniqueId, Function.identity()));

            Collection<ApiEntity> diffApiEntities = CollectionUtils.subtract(apiEntities, oldApiEntities.values());

            List<ApiHistoryEntity> apiHistoryEntities = apiRepository.insert(apiEntities).stream()
                .map(apiEntity -> ApiHistoryEntity.builder().record(apiHistoryMapper.toApiHistoryDetail(apiEntity))
                    .build())
                .collect(
                    Collectors.toList());
            apiHistoryRepository.insert(apiHistoryEntities);
        }
        return true;
    }


    @Override
    public ApiResponse findById(String id) {
        try {
            Optional<ApiEntity> optional = apiRepository.findById(id);
            return apiMapper.toDto(optional.orElse(null));
        } catch (Exception e) {
            log.error("Failed to get the Api by id!", e);
            throw new ApiTestPlatformException(GET_API_BY_ID_ERROR);
        }
    }

    @Override
    public Page<ApiResponse> page(ApiPageRequest apiPageRequest) {
        try {
            PageDtoConverter.frontMapping(apiPageRequest);
            return customizedApiRepository.page(apiPageRequest);
        } catch (Exception e) {
            log.error("Failed to get the Api page!", e);
            throw new ApiTestPlatformException(GET_API_PAGE_ERROR);
        }
    }

    @Override
    public Boolean add(ApiRequest apiRequestDto) {
        log.info("ApiService-add()-params: [Api]={}", apiRequestDto.toString());
        try {
            ApiEntity apiEntity = apiMapper.toEntity(apiRequestDto);
            ApiEntity newApiEntity = apiRepository.insert(apiEntity);
            ApiHistoryEntity apiHistoryEntity = ApiHistoryEntity.builder()
                .record(apiHistoryMapper.toApiHistoryDetail(apiEntity)).build();
            apiHistoryRepository.insert(apiHistoryEntity);
        } catch (Exception e) {
            log.error("Failed to add the Api!", e);
            throw new ApiTestPlatformException(ADD_API_ERROR);
        }
        return true;
    }

    @Override
    public Boolean edit(ApiRequest apiRequestDto) {
        log.info("ApiService-edit()-params: [Api]={}", apiRequestDto.toString());
        try {
            ApiEntity apiEntity = apiMapper.toEntity(apiRequestDto);
            Optional<ApiEntity> optional = apiRepository.findById(apiEntity.getId());
            if (optional.isEmpty()) {
                return Boolean.FALSE;
            }
            ApiEntity newApiEntity = apiRepository.save(apiEntity);
            ApiHistoryEntity apiHistoryEntity = ApiHistoryEntity.builder()
                .record(apiHistoryMapper.toApiHistoryDetail(apiEntity)).build();
            apiHistoryRepository.insert(apiHistoryEntity);
        } catch (Exception e) {
            log.error("Failed to add the Api!", e);
            throw new ApiTestPlatformException(EDIT_API_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean delete(List<String> ids) {
        try {
            return customizedApiRepository.deleteByIds(ids);
        } catch (Exception e) {
            log.error("Failed to delete the Api!", e);
            throw new ApiTestPlatformException(DELETE_API_BY_ID_ERROR);
        }
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private DocumentDefinition<?> parserDocument(ApiImportRequest apiImportRequest) {
        DocumentDefinition<?> documentDefinition;
        DocumentType documentType = DocumentType.getType(apiImportRequest.getDocumentType());
        DocumentReader reader = documentType.getReader();
        String documentUrl = apiImportRequest.getDocumentUrl();
        MultipartFile file = apiImportRequest.getFile();
        String projectId = apiImportRequest.getProjectId();
        if (StringUtils.isNotBlank(documentUrl)) {
            documentDefinition = reader.readLocation(documentUrl, projectId);
        } else if (Objects.nonNull(file)) {
            try {
                documentDefinition = reader
                    .readContents(IOUtils.toString(file.getInputStream(), StandardCharsets.UTF_8), projectId);
            } catch (Exception e) {
                throw ExceptionUtils.mpe("Failed to read the file in DocumentType", e);
            }
        } else {
            throw ExceptionUtils
                .mpe("Not in the supported range(DocumentType=%s)", new Object[]{DocumentType.values()});
        }
        return documentDefinition;
    }

}
