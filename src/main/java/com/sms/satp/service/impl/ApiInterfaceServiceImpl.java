package com.sms.satp.service.impl;

import static com.sms.satp.utils.ApiSchemaUtil.getRefKey;
import static com.sms.satp.utils.ApiSchemaUtil.resolveApiSchemaMap;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.common.ErrorCode;
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
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
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
        ApiInterface apiInterface = ApiInterface.builder()
            .projectId(projectId)
            .build();
        Example<ApiInterface> example = Example.of(apiInterface);
        return apiInterfaceMapper.toDtoList(apiInterfaceRepository.findAll(example));
    }

    @Override
    public Page<ApiInterfaceDto> page(PageDto pageDto, String projectId) {
        ApiInterface apiInterface = ApiInterface.builder()
            .projectId(projectId)
            .build();
        Example<ApiInterface> example = Example.of(apiInterface);
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize(), sort);
        return apiInterfaceRepository.findAll(example, pageable)
            .map(apiInterfaceMapper::toDto);
    }

    @Override
    public void save(String url, String documentType, String projectId) {
        Optional<DocumentType> documentTypeOptional = Optional.ofNullable(
            DocumentType.resolve(documentType));
        if (documentTypeOptional.isPresent()) {
            ApiDocument apiDocument = documentFactory.create(url, documentTypeOptional.get());
            List<ApiInterface> apiInterfaces = convertApiPathsToApiInterfaces(
                apiDocument, projectId);
            apiInterfaceRepository.insert(apiInterfaces);
        } else {
            throw new ApiTestPlatformException(ErrorCode.DOCUMENT_TYPE_ERROR);
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
        apiInterfaceRepository.insert(apiInterfaceMapper.toEntity(apiInterfaceDto));
    }

    @Override
    public ApiInterfaceDto getApiInterfaceById(String id) {
        Optional<ApiInterface> optionalApiInterface = apiInterfaceRepository.findById(id);
        return apiInterfaceMapper.toDto(optionalApiInterface.orElse(null));
    }

    @Override
    public void deleteById(String id) {
        apiInterfaceRepository.deleteById(id);
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