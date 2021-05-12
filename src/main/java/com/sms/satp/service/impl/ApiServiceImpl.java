package com.sms.satp.service.impl;

import static com.sms.satp.common.constant.CommonFiled.ID;
import static com.sms.satp.common.constant.CommonFiled.MODIFY_DATE_TIME;
import static com.sms.satp.common.constant.CommonFiled.PROJECT_ID;
import static com.sms.satp.common.constant.CommonFiled.REMOVE;
import static com.sms.satp.common.exception.ErrorCode.ADD_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_API_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_PAGE_ERROR;

import com.sms.satp.common.enums.DocumentType;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.ApiPageRequest;
import com.sms.satp.dto.ApiRequest;
import com.sms.satp.dto.ApiResponse;
import com.sms.satp.dto.request.ApiImportRequest;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.api.ApiHistoryEntity;
import com.sms.satp.mapper.ApiMapper;
import com.sms.satp.parser.ApiDocumentTransformer;
import com.sms.satp.parser.DocumentReader;
import com.sms.satp.parser.common.DocumentDefinition;
import com.sms.satp.repository.ApiHistoryRepository;
import com.sms.satp.repository.ApiRepository;
import com.sms.satp.repository.ProjectEntityRepository;
import com.sms.satp.service.ApiService;
import com.sms.satp.service.condition.ApiFiled;
import com.sms.satp.utils.ExceptionUtils;
import com.sms.satp.utils.PageDtoConverter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class ApiServiceImpl implements ApiService {

    private final ProjectEntityRepository projectEntityRepository;
    private final ApiRepository apiRepository;
    private final ApiHistoryRepository apiHistoryRepository;
    private final ApiMapper apiMapper;
    private final MongoTemplate mongoTemplate;

    public ApiServiceImpl(ProjectEntityRepository projectEntityRepository,
        ApiRepository apiRepository, ApiHistoryRepository apiHistoryRepository, ApiMapper apiMapper,
        MongoTemplate mongoTemplate) {
        this.projectEntityRepository = projectEntityRepository;
        this.apiRepository = apiRepository;
        this.apiHistoryRepository = apiHistoryRepository;
        this.apiMapper = apiMapper;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean importDocument(ApiImportRequest apiImportRequest) {
        DocumentType documentType = DocumentType.getType(apiImportRequest.getDocumentType());
        DocumentDefinition definition = getDocumentParserResult(apiImportRequest);
        ApiDocumentTransformer transformer = documentType.getTransformer();
        List<ApiEntity> apiEntities = transformer.toApiEntities(definition);
//        List<ApiEntity> oldApiEntities = apiRepository
//            .findApiEntitiesByProjectId(apiImportRequest);
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
            ArrayList<AggregationOperation> aggregationOperations = new ArrayList<>();
            Query query = new Query();

            LookupOperation apiTagLookupOperation =
                LookupOperation.newLookup().from("ApiTag").localField("tagId").foreignField("_id").as("apiTag");
            aggregationOperations.add(apiTagLookupOperation);

            buildCriteria(apiPageRequest, query, aggregationOperations);

            Sort sort = Sort.by(Direction.fromString(apiPageRequest.getOrder()), apiPageRequest.getSort());
            aggregationOperations.add(Aggregation.sort(sort));

            int skipRecord = apiPageRequest.getPageNumber() * apiPageRequest.getPageSize();
            aggregationOperations.add(Aggregation.skip(Long.valueOf(skipRecord)));
            aggregationOperations.add(Aggregation.limit(apiPageRequest.getPageSize()));

            ProjectionOperation project = Aggregation.project(ApiResponse.class);
            ProjectionOperation projectionOperation = project.andInclude("apiTag.tagName");
            aggregationOperations.add(projectionOperation);

            Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
            long count = mongoTemplate.count(query, ApiEntity.class);
            if (count == 0L || skipRecord >= count) {
                return Page.empty();
            }
            List<ApiResponse> records = mongoTemplate.aggregate(aggregation, ApiEntity.class, ApiResponse.class)
                .getMappedResults();
            return new PageImpl<ApiResponse>(records,
                PageRequest.of(apiPageRequest.getPageNumber(), apiPageRequest.getPageSize(), sort), count);
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
            ApiEntity newApiEntity = apiRepository.save(apiEntity);
            ApiHistoryEntity apiHistoryEntity = ApiHistoryEntity.builder().record(newApiEntity).build();
            apiHistoryRepository.insert(apiHistoryEntity);
        } catch (Exception e) {
            log.error("Failed to add the Api!", e);
            throw new ApiTestPlatformException(EDIT_API_ERROR);
        }
        return true;
    }

    @Override
    public Boolean delete(String[] ids) {
        try {
            Query query = new Query(Criteria.where(ID).in(Arrays.asList(ids)));
            Update update = Update.update(REMOVE, Boolean.TRUE);
            update.set(MODIFY_DATE_TIME, LocalDateTime.now());
            mongoTemplate.updateMulti(query, update, ApiEntity.class);
        } catch (Exception e) {
            log.error("Failed to delete the Api!", e);
            throw new ApiTestPlatformException(DELETE_API_BY_ID_ERROR);
        }
        return true;
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

    private void buildCriteria(ApiPageRequest apiPageRequest, Query query,
        List<AggregationOperation> aggregationOperations) {
        Criteria projectIdCriteria = Criteria.where(PROJECT_ID).is(apiPageRequest.getProjectId());
        Criteria removedCriteria = Criteria.where(REMOVE).is(Boolean.FALSE);
        query.addCriteria(projectIdCriteria);
        query.addCriteria(removedCriteria);
        aggregationOperations.add(Aggregation.match(projectIdCriteria));
        aggregationOperations.add(Aggregation.match(removedCriteria));

        if (Objects.nonNull(apiPageRequest.getApiProtocol())) {
            Criteria criteria = Criteria.where(ApiFiled.API_PROTOCOL.getFiled()).in(apiPageRequest.getApiProtocol());
            aggregationOperations.add(Aggregation.match(criteria));
            query.addCriteria(criteria);
        }
        if (Objects.nonNull(apiPageRequest.getApiRequestParamType())) {
            Criteria criteria = Criteria.where(ApiFiled.API_REQUEST_PARAM_TYPE.getFiled())
                .in(apiPageRequest.getApiRequestParamType());
            aggregationOperations.add(Aggregation.match(criteria));
            query.addCriteria(criteria);
        }
        if (Objects.nonNull(apiPageRequest.getApiStatus())) {
            Criteria criteria = Criteria.where(ApiFiled.API_STATUS.getFiled()).in(apiPageRequest.getApiStatus());
            aggregationOperations.add(Aggregation.match(criteria));
            query.addCriteria(criteria);
        }
        if (Objects.nonNull(apiPageRequest.getGroupId())) {
            Criteria criteria = Criteria.where(ApiFiled.GROUP_ID.getFiled()).in(apiPageRequest.getGroupId());
            aggregationOperations.add(Aggregation.match(criteria));
            query.addCriteria(criteria);
        }
        if (Objects.nonNull(apiPageRequest.getRequestMethod())) {
            Criteria criteria = Criteria.where(ApiFiled.REQUEST_METHOD.getFiled())
                .in(apiPageRequest.getRequestMethod());
            aggregationOperations.add(Aggregation.match(criteria));
            query.addCriteria(criteria);
        }
        if (Objects.nonNull(apiPageRequest.getTagId())) {
            Criteria criteria = Criteria.where(ApiFiled.TAG_ID.getFiled()).in(apiPageRequest.getTagId());
            aggregationOperations.add(Aggregation.match(criteria));
            query.addCriteria(criteria);
        }

    }
}
