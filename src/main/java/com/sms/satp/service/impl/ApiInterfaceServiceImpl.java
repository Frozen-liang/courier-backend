package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.ADD_API_INTERFACE_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_API_INTERFACE_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.DOCUMENT_TYPE_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_API_INTERFACE_ERROR;
import static com.sms.satp.common.ErrorCode.GET_API_INTERFACE_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_API_INTERFACE_LIST_ERROR;
import static com.sms.satp.common.ErrorCode.GET_API_INTERFACE_PAGE_ERROR;
import static com.sms.satp.common.ErrorCode.PARSE_FILE_AND_SAVE_AS_APIINTERFACE_ERROR;
import static com.sms.satp.utils.ApiSchemaUtil.getRefKey;
import static com.sms.satp.utils.ApiSchemaUtil.resolveApiSchemaMap;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.ApiInterface;
import com.sms.satp.entity.Header;
import com.sms.satp.entity.Parameter;
import com.sms.satp.entity.dto.ApiInterfaceDto;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.mapper.ApiInterfaceMapper;
import com.sms.satp.parser.DocumentFactory;
import com.sms.satp.parser.common.DocumentType;
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
import com.sms.satp.utils.ApiHeaderConverter;
import com.sms.satp.utils.ApiParameterConverter;
import com.sms.satp.utils.ApiRequestBodyConverter;
import com.sms.satp.utils.ApiResponseConverter;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class ApiInterfaceServiceImpl implements ApiInterfaceService {

    private final ApiInterfaceRepository apiInterfaceRepository;
    private final DocumentFactory documentFactory;
    private final ApiInterfaceMapper apiInterfaceMapper;

    public ApiInterfaceServiceImpl(ApiInterfaceRepository apiInterfaceRepository,
        DocumentFactory documentFactory, ApiInterfaceMapper apiInterfaceMapper) {
        this.apiInterfaceRepository = apiInterfaceRepository;
        this.documentFactory = documentFactory;
        this.apiInterfaceMapper = apiInterfaceMapper;
    }

    @Override
    public List<ApiInterfaceDto> list(String projectId) {
        try {
            ApiInterface apiInterface = ApiInterface.builder()
                .projectId(projectId)
                .build();
            Example<ApiInterface> example = Example.of(apiInterface);
            return apiInterfaceMapper.toDtoList(apiInterfaceRepository.findAll(example));
        } catch (Exception e) {
            log.error("Failed to get the ApiInterface list!", e);
            throw new ApiTestPlatformException(GET_API_INTERFACE_LIST_ERROR);
        }
    }

    @Override
    public Page<ApiInterfaceDto> page(PageDto pageDto, String projectId) {
        try {
            ApiInterface apiInterface = ApiInterface.builder()
                .projectId(projectId)
                .build();
            Example<ApiInterface> example = Example.of(apiInterface);
            Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
            Pageable pageable = PageRequest.of(
                pageDto.getPageNumber(), pageDto.getPageSize(), sort);
            return apiInterfaceRepository.findAll(example, pageable)
                .map(apiInterfaceMapper::toDto);
        } catch (Exception e) {
            log.error("Failed to get the ApiInterface page!", e);
            throw new ApiTestPlatformException(GET_API_INTERFACE_PAGE_ERROR);
        }
    }

    @Override
    public void save(String location, String documentType, String projectId) {
        if (log.isDebugEnabled()) {
            log.debug(
                String.format("ApiInterfaceService-save()-Parameter: [location]%s, [documentType]%s, [projectId]%s",
                location, documentType, projectId));
        }
        Optional<DocumentType> documentTypeOptional = Optional.ofNullable(
            DocumentType.resolve(documentType.toUpperCase(Locale.getDefault())));
        if (documentTypeOptional.isPresent()) {
            try {
                ApiDocument apiDocument = documentFactory.create(
                    location, documentTypeOptional.get());
                List<ApiInterface> apiInterfaces = convertApiPathsToApiInterfaces(
                    apiDocument, projectId);
                apiInterfaceRepository.insert(apiInterfaces);
            } catch (Exception e) {
                log.error("Failed to parse the file and save as ApiInterface!", e);
                throw new ApiTestPlatformException(PARSE_FILE_AND_SAVE_AS_APIINTERFACE_ERROR);
            }
        } else {
            throw new ApiTestPlatformException(DOCUMENT_TYPE_ERROR);
        }
    }

    @Override
    public void save(MultipartFile multipartFile, String documentType, String projectId) throws IOException {
        File file = convertToFile(multipartFile);
        save(file.toString(), documentType, projectId);
        boolean delete = file.delete();
        if (!delete) {
            log.warn("Delete temp file failed");
        }
    }

    @Override
    public void add(ApiInterfaceDto apiInterfaceDto) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("ApiInterfaceService-add()-Parameter: %s",
                apiInterfaceDto.toString()));
        }
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
        if (log.isDebugEnabled()) {
            log.debug(String.format("ApiInterfaceService-edit()-Parameter: %s",
                apiInterfaceDto.toString()));
        }
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
    public ApiInterfaceDto getApiInterfaceById(String id) {
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

    private File convertToFile(MultipartFile multipartFile) throws IOException {
        File file = File.createTempFile("tmp", null);
        multipartFile.transferTo(file);
        return file;
    }

    private List<ApiInterface> convertApiPathsToApiInterfaces(
        ApiDocument apiDocument, String projectId) {
        List<ApiInterface> apiInterfaces = new ArrayList<>();
        List<ApiPath> apiPaths = apiDocument.getPaths();
        Map<String, ApiSchema> apiSchema = apiDocument.getSchemas();
        resolveApiSchemaMap(apiSchema, apiSchema);
        apiPaths.forEach((ApiPath apiPath) ->
            apiPath.getOperations().forEach((ApiOperation apiOperation) -> {
                apiInterfaces.add(
                    apiPathOperationResolver(apiOperation, apiSchema, apiPath, projectId));
            })
        );
        return apiInterfaces;
    }

    private ApiInterface apiPathOperationResolver(ApiOperation apiOperation,
        Map<String, ApiSchema> apiSchema, ApiPath apiPath, String projectId) {
        Optional<ApiRequestBody> apiRequestBodyOptional = Optional
            .ofNullable(apiOperation.getApiRequestBody());
        Optional<ApiResponse> apiResponseOptional = Optional
            .ofNullable(apiOperation.getApiResponse());
        Optional<String> requestBodyRefOptional = apiRequestBodyOptional
            .map(ApiRequestBody::getSchema).map(ApiSchema::getRef);
        Optional<String> responseRefOptional = apiResponseOptional
            .map(ApiResponse::getSchema).map(ApiSchema::getRef);
        requestBodyRefOptional.ifPresent(requestRef -> apiRequestBodyOptional.get()
            .setSchema(apiSchema.get(getRefKey(requestRef))));
        responseRefOptional.ifPresent(responseRdf -> apiResponseOptional.get()
            .setSchema(apiSchema.get(getRefKey(responseRdf))));
        ApiInterface apiInterface = ApiInterface.builder()
            .id(new ObjectId().toString())
            .method(HttpMethod.resolve(apiOperation.getHttpMethod().name()))
            .projectId(projectId)
            .tag(apiOperation.getTags())
            .title(apiOperation.getSummary())
            .path(apiPath.getPath())
            .description(apiOperation.getDescription())
            .responseHeaders(
                apiResponseOptional.map(ApiResponse::getHeaders).isPresent()
                    ? ApiHeaderConverter.CONVERT_TO_HEADER.apply(apiResponseOptional.map(ApiResponse::getHeaders).get())
                    : null)
            .requestBody(
                apiRequestBodyOptional.map(ApiRequestBodyConverter.CONVERT_TO_REQUEST_BODY).orElse(null))
            .response(
                apiResponseOptional.map(ApiResponseConverter.CONVERT_TO_RESPONSE).orElse(null))
            .createDateTime(LocalDateTime.now())
            .build();
        apiParameterResolver(apiInterface, apiOperation.getParameters());
        return apiInterface;
    }

    private void apiParameterResolver(ApiInterface apiInterface, List<ApiParameter> apiParameters) {
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
                    requestHeaders.add(ApiParameterConverter.CONVERT_TO_HEADER.apply(apiParameter));
                    break;
                default:
                    break;
            }
        }
        apiInterface.setQueryParams(queryParams.isEmpty() ? null : queryParams);
        apiInterface.setRequestHeaders(requestHeaders.isEmpty() ? null : requestHeaders);
        apiInterface.setPathParams(pathParams.isEmpty() ? null : pathParams);
    }
}