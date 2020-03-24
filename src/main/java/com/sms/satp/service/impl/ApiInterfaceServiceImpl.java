package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.ADD_API_INTERFACE_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_API_INTERFACE_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_API_INTERFACE_ERROR;
import static com.sms.satp.common.ErrorCode.GET_API_INTERFACE_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_API_INTERFACE_PAGE_ERROR;
import static com.sms.satp.common.ErrorCode.PARSE_TO_API_INTERFACE_ERROR;
import static com.sms.satp.utils.ApiSchemaUtil.removeSchemaMapRef;
import static com.sms.satp.utils.ApiSchemaUtil.splitKeyFromRef;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.ApiInterface;
import com.sms.satp.entity.ApiInterface.ApiInterfaceBuilder;
import com.sms.satp.entity.DocumentImport;
import com.sms.satp.entity.Header;
import com.sms.satp.entity.Parameter;
import com.sms.satp.entity.dto.ApiInterfaceDto;
import com.sms.satp.entity.dto.DocumentImportDto;
import com.sms.satp.entity.dto.ImportWay;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.SaveMode;
import com.sms.satp.mapper.ApiInterfaceMapper;
import com.sms.satp.mapper.DocumentImportMapper;
import com.sms.satp.parser.DocumentFactory;
import com.sms.satp.parser.common.HttpMethod;
import com.sms.satp.parser.model.ApiDocument;
import com.sms.satp.parser.model.ApiOperation;
import com.sms.satp.parser.model.ApiParameter;
import com.sms.satp.parser.model.ApiPath;
import com.sms.satp.parser.model.ApiRequestBody;
import com.sms.satp.parser.model.ApiResponse;
import com.sms.satp.parser.schema.ApiSchema;
import com.sms.satp.repository.ApiInterfaceRepository;
import com.sms.satp.service.ApiInterfaceService;
import com.sms.satp.service.InterfaceGroupService;
import com.sms.satp.utils.ApiHeaderConverter;
import com.sms.satp.utils.ApiParameterConverter;
import com.sms.satp.utils.ApiRequestBodyConverter;
import com.sms.satp.utils.ApiResponseConverter;
import com.sms.satp.utils.PageDtoConverter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApiInterfaceServiceImpl implements ApiInterfaceService {

    private static final Integer FIRST_TAG = 0;
    private static final String ALL_GROUP_FLAG = "-1";

    private final ApiInterfaceRepository apiInterfaceRepository;
    private final DocumentFactory documentFactory;
    private final ApiInterfaceMapper apiInterfaceMapper;
    private final DocumentImportMapper documentImportMapper;
    private final InterfaceGroupService interfaceGroupService;

    public ApiInterfaceServiceImpl(ApiInterfaceRepository apiInterfaceRepository,
        DocumentFactory documentFactory, ApiInterfaceMapper apiInterfaceMapper,
        DocumentImportMapper documentImportMapper, InterfaceGroupService interfaceGroupService) {
        this.apiInterfaceRepository = apiInterfaceRepository;
        this.documentFactory = documentFactory;
        this.apiInterfaceMapper = apiInterfaceMapper;
        this.documentImportMapper = documentImportMapper;
        this.interfaceGroupService = interfaceGroupService;
    }

    @Override
    public Page<ApiInterfaceDto> page(PageDto pageDto, String projectId, String groupId) {
        try {
            PageDtoConverter.frontMapping(pageDto);
            ApiInterfaceBuilder apiInterfaceBuilder = ApiInterface.builder()
                .projectId(projectId);
            if (!StringUtils.equals(groupId, ALL_GROUP_FLAG)) {
                apiInterfaceBuilder.groupId(groupId);
            }
            Example<ApiInterface> example = Example.of(apiInterfaceBuilder.build());
            Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
            Pageable pageable = PageRequest.of(
                pageDto.getPageNumber(), pageDto.getPageSize(), sort);
            return apiInterfaceRepository.findAll(example, pageable)
                .map(apiInterfaceMapper::toDtoPage);
        } catch (Exception e) {
            log.error("Failed to get the ApiInterface page!", e);
            throw new ApiTestPlatformException(GET_API_INTERFACE_PAGE_ERROR);
        }
    }


    @Override
    public void add(ApiInterfaceDto apiInterfaceDto) {
        log.info(
            "ApiInterfaceService-add()-params: [ApiInterface]={}", apiInterfaceDto.toString());
        try {
            ApiInterface apiInterface = apiInterfaceMapper.toEntity(apiInterfaceDto);
            apiInterface.setId(new ObjectId().toString());
            apiInterface.setCreateDateTime(LocalDateTime.now());
            apiInterfaceRepository.insert(apiInterface);
        } catch (Exception e) {
            log.error("Failed to add the ApiInterface!", e);
            throw new ApiTestPlatformException(ADD_API_INTERFACE_ERROR);
        }
    }

    @Override
    public void edit(ApiInterfaceDto apiInterfaceDto) {
        log.info(
            "ApiInterfaceService-edit()-params: [ApiInterface]={}", apiInterfaceDto.toString());
        try {
            ApiInterface apiInterface = apiInterfaceMapper.toEntity(apiInterfaceDto);
            Optional<ApiInterface> apiInterfaceOptional = apiInterfaceRepository
                .findById(apiInterface.getId());
            apiInterfaceOptional.ifPresent(apiInterfaceFindById -> {
                apiInterface.setCreateDateTime(apiInterfaceFindById.getCreateDateTime());
                apiInterface.setModifyDateTime(LocalDateTime.now());
                apiInterfaceRepository.save(apiInterface);
            });
        } catch (Exception e) {
            log.error("Failed to add the ApiInterface!", e);
            throw new ApiTestPlatformException(EDIT_API_INTERFACE_ERROR);
        }
    }

    @Override
    public ApiInterfaceDto findById(String id) {
        try {
            Optional<ApiInterface> optionalApiInterface = apiInterfaceRepository.findById(id);
            return apiInterfaceMapper.toDto(optionalApiInterface.orElse(null));
        } catch (Exception e) {
            log.error("Failed to get the ApiInterface by id!", e);
            throw new ApiTestPlatformException(GET_API_INTERFACE_BY_ID_ERROR);
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            apiInterfaceRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Failed to delete the ApiInterface!", e);
            throw new ApiTestPlatformException(DELETE_API_INTERFACE_BY_ID_ERROR);
        }
    }

    @Override
    public void importDocument(DocumentImportDto documentImportDto, ImportWay importWay) {
        DocumentImport documentImport = documentImportMapper.convert(documentImportDto);
        log.info(
            "ApiInterfaceService-importDocument()-params: [documentType]={}, [URL]={}, [Content]={}, [projectId]={}",
            documentImport.getType(), documentImport.getUrl(),
            documentImport.getContent(), documentImport.getProjectId());
        ApiDocument apiDocument = importWay.getExecutor().unchecked().apply(documentImport, documentFactory);
        List<ApiInterface> apiInterfaces = parseApiDocumentToApiInterfaces(apiDocument, documentImport.getProjectId());
        saveInterfacesByType(apiInterfaces, documentImport.getSaveMode());
    }

    private void saveInterfacesByType(List<ApiInterface> apiInterfaceList, SaveMode saveMode) {
        apiInterfaceList.forEach(apiInterface -> {
            ApiInterface apiInterfaceExample = ApiInterface.builder()
                .method(apiInterface.getMethod())
                .path(apiInterface.getPath())
                .projectId(apiInterface.getProjectId()).build();
            Example<ApiInterface> example = Example.of(apiInterfaceExample);
            Optional<ApiInterface> apiInterfaceOptional = apiInterfaceRepository.findOne(example);
            if (apiInterfaceOptional.isPresent()) {
                if (saveMode.matches(SaveMode.COVER.name())) {
                    apiInterface.setId(apiInterfaceOptional.get().getId());
                    apiInterfaceRepository.save(apiInterface);
                }
            } else {
                apiInterfaceRepository.save(apiInterface);
            }
        });
    }


    private List<ApiInterface> parseApiDocumentToApiInterfaces(ApiDocument apiDocument, String projectId) {
        try {
            List<ApiInterface> apiInterfaces = new ArrayList<>();
            List<ApiPath> apiPaths = apiDocument.getPaths();
            Map<String, ApiSchema> apiSchemaMap = apiDocument.getSchemas();
            removeSchemaMapRef(apiSchemaMap, apiSchemaMap);
            apiPaths.forEach(apiPath ->
                apiPath.getOperations().forEach(apiOperation ->
                    apiInterfaces.add(
                        apiPathOperationResolver(apiOperation, apiSchemaMap, apiPath, projectId))
                )
            );
            return apiInterfaces;
        } catch (Exception e) {
            log.error("Failed to parse the ApiDocument!", e);
            throw new ApiTestPlatformException(PARSE_TO_API_INTERFACE_ERROR);
        }
    }

    private ApiInterface apiPathOperationResolver(ApiOperation apiOperation,
        Map<String, ApiSchema> apiSchemaMap, ApiPath apiPath, String projectId) {
        Optional<ApiRequestBody> apiRequestBodyOptional = Optional.ofNullable(apiOperation.getApiRequestBody());
        Optional<ApiResponse> apiResponseOptional = Optional.ofNullable(apiOperation.getApiResponse());
        Optional<String> requestBodyRefOptional = apiRequestBodyOptional.map(
            ApiRequestBody::getSchema).map(ApiSchema::getRef);
        Optional<String> responseRefOptional = apiResponseOptional.map(ApiResponse::getSchema).map(ApiSchema::getRef);
        requestBodyRefOptional.ifPresent(requestRef -> {
            ApiSchema apiSchema = apiSchemaMap.get(splitKeyFromRef(requestRef));
            apiRequestBodyOptional.get().setSchema(apiSchema);
        });
        responseRefOptional.ifPresent(responseRef -> {
            ApiSchema apiSchema = apiSchemaMap.get(splitKeyFromRef(responseRef));
            apiResponseOptional.get().setSchema(apiSchema);
        });
        String groupId = getGroupIdByOperationTags(apiOperation, projectId);
        ApiInterface.ApiInterfaceBuilder apiInterfaceBuilder = ApiInterface.builder()
            .id(new ObjectId().toString())
            .method(HttpMethod.resolve(apiOperation.getHttpMethod().name()))
            .projectId(projectId)
            .groupId(groupId)
            .tag(apiOperation.getTags())
            .title(StringUtils.isBlank(apiOperation.getSummary()) ? apiPath.getPath() : apiOperation.getSummary())
            .path(apiPath.getPath())
            .description(apiOperation.getDescription())
            .responseHeaders(null)
            .requestBody(
                apiRequestBodyOptional.map(ApiRequestBodyConverter.CONVERT_TO_REQUEST_BODY).orElse(null))
            .response(
                apiResponseOptional.map(ApiResponseConverter.CONVERT_TO_RESPONSE).orElse(null))
            .createDateTime(LocalDateTime.now());
        apiResponseOptional.map(ApiResponse::getHeaders).ifPresent(apiHeaders ->
            apiInterfaceBuilder.responseHeaders(ApiHeaderConverter.CONVERT_TO_HEADER.apply(apiHeaders))
        );
        addParameterToInterface(apiInterfaceBuilder, apiOperation.getParameters());
        return apiInterfaceBuilder.build();
    }

    private String getGroupIdByOperationTags(ApiOperation apiOperation, String projectId) {
        List<String> tags = apiOperation.getTags();
        return tags.isEmpty() ? null : interfaceGroupService.addGroupByNameAndReturnId(
            tags.get(FIRST_TAG), projectId);
    }

    private void addParameterToInterface(ApiInterfaceBuilder apiInterfaceBuilder, List<ApiParameter> apiParameters) {
        List<Header> requestHeaders = new ArrayList<>();
        List<Parameter> queryParams = new ArrayList<>();
        List<Parameter> pathParams = new ArrayList<>();
        for (ApiParameter apiParameter : apiParameters) {
            switch (apiParameter.getIn()) {
                case QUERY:
                    queryParams.add(ApiParameterConverter.CONVERT_TO_PARAMETER.apply(apiParameter));
                    break;
                case PATH:
                    pathParams.add(ApiParameterConverter.CONVERT_TO_PARAMETER.apply(apiParameter));
                    break;
                case HEADER:
                case COOKIE:
                    requestHeaders.add(ApiParameterConverter.CONVERT_TO_HEADER.apply(apiParameter));
                    break;
                default:
                    break;
            }
        }
        apiInterfaceBuilder
            .responseHeaders(requestHeaders.isEmpty() ? null : requestHeaders)
            .queryParams(queryParams.isEmpty() ? null : queryParams)
            .pathParams(pathParams.isEmpty() ? null : pathParams);
    }
}