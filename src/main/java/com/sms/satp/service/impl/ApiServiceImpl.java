package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.ADD_API_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_API_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_API_ERROR;
import static com.sms.satp.common.ErrorCode.GET_API_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_API_PAGE_ERROR;
import static com.sms.satp.common.constant.CommonFiled.ID;
import static com.sms.satp.common.constant.CommonFiled.MODIFY_DATE_TIME;
import static com.sms.satp.common.constant.CommonFiled.PROJECT_ID;
import static com.sms.satp.common.constant.CommonFiled.REMOVE;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.dto.ApiImportRequest;
import com.sms.satp.dto.ApiPageRequestDto;
import com.sms.satp.dto.ApiRequestDto;
import com.sms.satp.dto.ApiResponseDto;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.api.ApiHistoryEntity;
import com.sms.satp.entity.project.ProjectEntity;
import com.sms.satp.mapper.ApiMapper;
import com.sms.satp.parser.ApiDocumentTransformer;
import com.sms.satp.parser.DocumentReader;
import com.sms.satp.parser.common.DocumentParserResult;
import com.sms.satp.parser.common.DocumentType;
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
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation.ProjectionOperationBuilder;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
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
        DocumentType documentType = DocumentType.resolve(apiImportRequest.getDocumentType());
        DocumentReader reader = documentType.getReader();
        ApiDocumentTransformer transformer = documentType.getTransformer();
        String documentUrl = apiImportRequest.getDocumentUrl();
        MultipartFile file = apiImportRequest.getFile();
        DocumentParserResult content = getDocumentParserResult(reader, documentUrl, file);
        ProjectEntity projectEntity = projectEntityRepository.insert(transformer.toProjectEntity(content));
        List<ApiEntity> apiEntities = transformer.toApiEntities(content, projectEntity.getId());
        List<ApiHistoryEntity> apiHistoryEntities = apiRepository.insert(apiEntities).stream()
            .map(apiEntity -> ApiHistoryEntity.builder().record(apiEntity).build()).collect(
                Collectors.toList());
        apiHistoryRepository.insert(apiHistoryEntities);
        return true;
    }

    @Override
    public ApiResponseDto findById(String id) {
        try {
            Optional<ApiEntity> optional = apiRepository.findById(id);
            return apiMapper.toDto(optional.orElse(null));
        } catch (Exception e) {
            log.error("Failed to get the Api by id!", e);
            throw new ApiTestPlatformException(GET_API_BY_ID_ERROR);
        }
    }

    @Override
    public Page<ApiResponseDto> page(ApiPageRequestDto apiPageRequestDto) {
        try {
            PageDtoConverter.frontMapping(apiPageRequestDto);
            ArrayList<AggregationOperation> aggregationOperations = new ArrayList<>();
            Query query = new Query();

            LookupOperation apiTagLookupOperation =
                LookupOperation.newLookup().from("ApiTag").localField("tagId").foreignField("_id").as("apiTag");
            aggregationOperations.add(apiTagLookupOperation);

            buildCriteria(apiPageRequestDto, query, aggregationOperations);

            Sort sort = Sort.by(Direction.fromString(apiPageRequestDto.getOrder()), apiPageRequestDto.getSort());
            aggregationOperations.add(Aggregation.sort(sort));

            int skipRecord = apiPageRequestDto.getPageNumber() * apiPageRequestDto.getPageSize();
            aggregationOperations.add(Aggregation.skip(Long.valueOf(skipRecord)));
            aggregationOperations.add(Aggregation.limit(apiPageRequestDto.getPageSize()));

            ProjectionOperation project = Aggregation.project(ApiResponseDto.class);
            ProjectionOperation projectionOperation = project.andInclude("apiTag.tagName");
            aggregationOperations.add(projectionOperation);

            Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
            long count = mongoTemplate.count(query, ApiEntity.class);
            if (count == 0L || skipRecord >= count) {
                return Page.empty();
            }
            List<ApiResponseDto> records = mongoTemplate.aggregate(aggregation, ApiEntity.class, ApiResponseDto.class)
                .getMappedResults();
            return new PageImpl<ApiResponseDto>(records,
                PageRequest.of(apiPageRequestDto.getPageNumber(), apiPageRequestDto.getPageSize(), sort), count);
        } catch (Exception e) {
            log.error("Failed to get the Api page!", e);
            throw new ApiTestPlatformException(GET_API_PAGE_ERROR);
        }
    }


    @Override
    public Boolean add(ApiRequestDto apiRequestDto) {
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
    public Boolean edit(ApiRequestDto apiRequestDto) {
        log.info("ApiService-edit()-params: [Api]={}", apiRequestDto.toString());
        try {
            ApiEntity apiEntity = apiMapper.toEntity(apiRequestDto);
            apiRepository.findById(apiEntity.getId())
                .ifPresent((oldApiEntity) -> {
                    apiEntity.setCreateUserId(oldApiEntity.getCreateUserId());
                    apiEntity.setCreateDateTime(oldApiEntity.getCreateDateTime());
                    ApiEntity newApiEntity = apiRepository.save(apiEntity);
                    ApiHistoryEntity apiHistoryEntity = ApiHistoryEntity.builder().record(newApiEntity).build();
                    apiHistoryRepository.insert(apiHistoryEntity);
                });
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

    private DocumentParserResult getDocumentParserResult(DocumentReader reader, String documentUrl,
        MultipartFile file) {
        DocumentParserResult content;
        if (StringUtils.isNotBlank(documentUrl)) {
            content = reader.readLocation(documentUrl);
        } else if (Objects.nonNull(file)) {
            try {
                content = reader.readContents(IOUtils.toString(file.getInputStream(), StandardCharsets.UTF_8));
            } catch (Exception e) {
                throw ExceptionUtils.mpe("Failed to read the file in DocumentType", e);
            }
        } else {
            throw ExceptionUtils
                .mpe("Not in the supported range(DocumentType=%s)", new Object[]{DocumentType.values()});
        }
        return content;
    }

    private void buildCriteria(ApiPageRequestDto apiPageRequestDto, Query query,
        List<AggregationOperation> aggregationOperations) {
        Criteria projectIdCriteria = Criteria.where(PROJECT_ID).is(apiPageRequestDto.getProjectId());
        Criteria removedCriteria = Criteria.where(REMOVE).is(Boolean.FALSE);
        query.addCriteria(projectIdCriteria);
        query.addCriteria(removedCriteria);
        aggregationOperations.add(Aggregation.match(projectIdCriteria));
        aggregationOperations.add(Aggregation.match(removedCriteria));

        if (Objects.nonNull(apiPageRequestDto.getApiProtocol())) {
            Criteria criteria = Criteria.where(ApiFiled.API_PROTOCOL.getFiled()).in(apiPageRequestDto.getApiProtocol());
            aggregationOperations.add(Aggregation.match(criteria));
            query.addCriteria(criteria);
        }
        if (Objects.nonNull(apiPageRequestDto.getApiRequestParamType())) {
            Criteria criteria = Criteria.where(ApiFiled.API_REQUEST_PARAM_TYPE.getFiled())
                .in(apiPageRequestDto.getApiRequestParamType());
            aggregationOperations.add(Aggregation.match(criteria));
            query.addCriteria(criteria);
        }
        if (Objects.nonNull(apiPageRequestDto.getApiStatus())) {
            Criteria criteria = Criteria.where(ApiFiled.API_STATUS.getFiled()).in(apiPageRequestDto.getApiStatus());
            aggregationOperations.add(Aggregation.match(criteria));
            query.addCriteria(criteria);
        }
        if (Objects.nonNull(apiPageRequestDto.getGroupId())) {
            Criteria criteria = Criteria.where(ApiFiled.GROUP_ID.getFiled()).in(apiPageRequestDto.getGroupId());
            aggregationOperations.add(Aggregation.match(criteria));
            query.addCriteria(criteria);
        }
        if (Objects.nonNull(apiPageRequestDto.getRequestMethod())) {
            Criteria criteria = Criteria.where(ApiFiled.REQUEST_METHOD.getFiled())
                .in(apiPageRequestDto.getRequestMethod());
            aggregationOperations.add(Aggregation.match(criteria));
            query.addCriteria(criteria);
        }
        if (Objects.nonNull(apiPageRequestDto.getTagId())) {
            Criteria criteria = Criteria.where(ApiFiled.TAG_ID.getFiled()).in(apiPageRequestDto.getTagId());
            aggregationOperations.add(Aggregation.match(criteria));
            query.addCriteria(criteria);
        }

    }
}
