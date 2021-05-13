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
import com.sms.satp.mapper.ApiMapper;
import com.sms.satp.parser.ApiDocumentTransformer;
import com.sms.satp.parser.DocumentReader;
import com.sms.satp.parser.common.DocumentDefinition;
import com.sms.satp.repository.ApiHistoryRepository;
import com.sms.satp.repository.ApiRepository;
import com.sms.satp.repository.CustomizedApiRepository;
import com.sms.satp.repository.ProjectEntityRepository;
import com.sms.satp.service.ApiService;
import com.sms.satp.utils.ExceptionUtils;
import com.sms.satp.utils.PageDtoConverter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class ApiServiceImpl implements ApiService {

    private final ProjectEntityRepository projectEntityRepository;
    private final ApiRepository apiRepository;
    private final ApiHistoryRepository apiHistoryRepository;
    private final ApiMapper apiMapper;
    private final CustomizedApiRepository customizedApiRepository;

    public ApiServiceImpl(ProjectEntityRepository projectEntityRepository,
        ApiRepository apiRepository, ApiHistoryRepository apiHistoryRepository, ApiMapper apiMapper,
        CustomizedApiRepository customizedApiRepository) {
        this.projectEntityRepository = projectEntityRepository;
        this.apiRepository = apiRepository;
        this.apiHistoryRepository = apiHistoryRepository;
        this.apiMapper = apiMapper;
        this.customizedApiRepository = customizedApiRepository;
    }

    @Override
    public boolean importDocument(ApiImportRequest apiImportRequest) {
        DocumentType documentType = DocumentType.getType(apiImportRequest.getDocumentType());
        DocumentDefinition definition = getDocumentParserResult(apiImportRequest);
        ApiDocumentTransformer transformer = documentType.getTransformer();
        List<ApiEntity> apiEntities = transformer.toApiEntities(definition);
        /*List<ApiEntity> oldApiEntities = apiRepository
            .findApiEntitiesByProjectId(apiImportRequest);*/
        List<ApiHistoryEntity> apiHistoryEntities = apiRepository.insert(apiEntities).stream()
            .map(apiEntity -> ApiHistoryEntity.builder().record(apiEntity).build()).collect(
                Collectors.toList());
        apiHistoryRepository.insert(apiHistoryEntities);
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
            ApiHistoryEntity apiHistoryEntity = ApiHistoryEntity.builder().record(newApiEntity).build();
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
            ApiHistoryEntity apiHistoryEntity = ApiHistoryEntity.builder().record(newApiEntity).build();
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

    private DocumentDefinition getDocumentParserResult(ApiImportRequest apiImportRequest) {
        DocumentDefinition content;
        DocumentType documentType = DocumentType.getType(apiImportRequest.getDocumentType());
        DocumentReader reader = documentType.getReader();
        String documentUrl = apiImportRequest.getDocumentUrl();
        MultipartFile file = apiImportRequest.getFile();
        String projectId = apiImportRequest.getProjectId();
        if (StringUtils.isNotBlank(documentUrl)) {
            content = reader.readLocation(documentUrl, projectId);
        } else if (Objects.nonNull(file)) {
            try {
                content = reader
                    .readContents(IOUtils.toString(file.getInputStream(), StandardCharsets.UTF_8), projectId);
            } catch (Exception e) {
                throw ExceptionUtils.mpe("Failed to read the file in DocumentType", e);
            }
        } else {
            throw ExceptionUtils
                .mpe("Not in the supported range(DocumentType=%s)", new Object[]{DocumentType.values()});
        }
        return content;
    }
}
