package com.sms.satp.service.impl;

import static com.sms.satp.common.exception.ErrorCode.ADD_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_API_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_PAGE_ERROR;

import com.sms.satp.common.enums.DocumentType;
import com.sms.satp.common.enums.ImportStatus;
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
import com.sms.satp.repository.ProjectImportFlowRepository;
import com.sms.satp.service.ApiService;
import com.sms.satp.utils.ExceptionUtils;
import com.sms.satp.utils.MD5Util;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
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

    private final ApiRepository apiRepository;
    private final ApiHistoryRepository apiHistoryRepository;
    private final ApiMapper apiMapper;
    private final ApiHistoryMapper apiHistoryMapper;
    private final CustomizedApiRepository customizedApiRepository;
    private final ApiGroupRepository apiGroupRepository;
    private final ProjectImportFlowRepository projectImportFlowRepository;
    private ApplicationContext applicationContext;

    public ApiServiceImpl(ApiRepository apiRepository, ApiHistoryRepository apiHistoryRepository, ApiMapper apiMapper,
        ApiHistoryMapper apiHistoryMapper, CustomizedApiRepository customizedApiRepository,
        ApiGroupRepository apiGroupRepository,
        ProjectImportFlowRepository projectImportFlowRepository) {
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
        log.info("The project whose Id is [{}] starts to import API documents.", apiImportRequest.getProjectId());
        DocumentType documentType = DocumentType.getType(apiImportRequest.getDocumentType());
        DocumentDefinition definition = parserDocument(apiImportRequest);
        ApiDocumentTransformer<?> transformer = documentType.getTransformer();
        Set<ApiGroupEntity> apiGroupEntities = transformer.toApiGroupEntities(definition,
            (apiGroupEntity -> apiGroupEntity.setProjectId(apiImportRequest.getProjectId())));

        List<ApiGroupEntity> oldGroupEntities =
            apiGroupRepository.findApiGroupEntitiesByProjectId(apiImportRequest.getProjectId());
        Collection<ApiGroupEntity> unsavedGroupEntities = CollectionUtils
            .subtract(apiGroupEntities, oldGroupEntities);
        List<ApiGroupEntity> newApiGroupEntities = apiGroupRepository.saveAll(unsavedGroupEntities);
        newApiGroupEntities.addAll(oldGroupEntities);

        Map<String, String> groupMapping = newApiGroupEntities.stream()
            .collect(Collectors.toMap(ApiGroupEntity::getName, ApiGroupEntity::getId));

        List<ApiEntity> apiEntities = transformer.toApiEntities(definition, apiEntity -> {
            apiEntity.setProjectId(apiImportRequest.getProjectId());
            apiEntity.setGroupId(groupMapping.get(apiEntity.getGroupId()));
            apiEntity.setMd5(MD5Util.getMD5(apiEntity));
        });

        List<ApiDocumentChecker> apiDocumentCheckers = documentType.getApiDocumentCheckers();
        boolean allCheckPass = apiDocumentCheckers.stream()
            .allMatch(apiDocumentChecker -> apiDocumentChecker
                .check(apiEntities, projectImportFlowEntity, this.applicationContext));

        if (allCheckPass) {

            Map<String, ApiEntity> oldApiEntities = apiRepository
                .findApiEntitiesByProjectIdAndSwaggerIdNotNull(apiImportRequest.getProjectId()).stream()
                .collect(Collectors.toConcurrentMap(ApiEntity::getSwaggerId, Function.identity()));
            Collection<ApiEntity> diffApiEntities = CollectionUtils.subtract(apiEntities, oldApiEntities.values());
            diffApiEntities.parallelStream()
                .filter(apiEntity -> oldApiEntities.containsKey(apiEntity.getSwaggerId()))
                .forEach(apiEntity -> {
                    ApiEntity oldApiEntity = oldApiEntities.get(apiEntity.getSwaggerId());
                    apiEntity.setId(oldApiEntity.getId());
                    apiEntity.setApiStatus(oldApiEntity.getApiStatus());
                    apiEntity.setPreInject(oldApiEntity.getPreInject());
                    apiEntity.setPostInject(oldApiEntity.getPostInject());
                    apiEntity.setTagId(oldApiEntity.getTagId());
                    apiEntity.setCreateUserId(oldApiEntity.getCreateUserId());
                    apiEntity.setCreateDateTime(oldApiEntity.getCreateDateTime());
                });

            if (MapUtils.isNotEmpty(oldApiEntities)) {
                List<String> swaggerIds =
                    apiEntities.stream().map(ApiEntity::getSwaggerId).collect(Collectors.toList());
                Predicate<String> existSwaggerId = swaggerIds::contains;
                Collection<ApiEntity> invalidApiEntities = oldApiEntities.values().stream()
                    .filter(apiEntity -> existSwaggerId.negate().test(apiEntity.getSwaggerId()))
                    .collect(Collectors.toList());
                log.info("Remove expired API=[{}]",
                    invalidApiEntities.stream().map(ApiEntity::getApiPath).collect(Collectors.joining(",")));
                apiRepository.deleteAll(invalidApiEntities);
            }
            if (CollectionUtils.isNotEmpty(diffApiEntities)) {

                List<ApiHistoryEntity> apiHistoryEntities = apiRepository.saveAll(diffApiEntities).stream()
                    .map(apiEntity -> ApiHistoryEntity.builder()
                        .record(apiHistoryMapper.toApiHistoryDetail(apiEntity)).build())
                    .collect(Collectors.toList());

                apiHistoryRepository.insert(apiHistoryEntities);
            }
            if (log.isDebugEnabled()) {
                log.debug("The project whose Id is [{}],Update API documents in total [{}].",
                    apiImportRequest.getProjectId(), diffApiEntities.size());
            }
            projectImportFlowEntity.setImportStatus(ImportStatus.SUCCESS);
            projectImportFlowEntity.setEndTime(LocalDateTime.now());
            projectImportFlowRepository.save(projectImportFlowEntity);
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
