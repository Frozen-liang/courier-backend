package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.ADD_API_INTERFACE_ERROR;
import static com.sms.satp.common.ErrorCode.ADD_INTERFACE_GROUP_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_API_INTERFACE_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_INTERFACE_GROUP_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.DOCUMENT_TYPE_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_API_INTERFACE_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_INTERFACE_GROUP_ERROR;
import static com.sms.satp.common.ErrorCode.GET_API_INTERFACE_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_API_INTERFACE_PAGE_ERROR;
import static com.sms.satp.common.ErrorCode.GET_INTERFACE_GROUP_LIST_ERROR;
import static com.sms.satp.common.ErrorCode.PARSE_TO_APIINTERFACE_ERROR;
import static com.sms.satp.utils.ApiSchemaUtil.removeSchemaMapRef;
import static com.sms.satp.utils.ApiSchemaUtil.splitKeyFromRef;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.ApiInterface;
import com.sms.satp.entity.ApiInterface.ApiInterfaceBuilder;
import com.sms.satp.entity.Header;
import com.sms.satp.entity.InterfaceGroup;
import com.sms.satp.entity.Parameter;
import com.sms.satp.entity.dto.ApiInterfaceDto;
import com.sms.satp.entity.dto.DataImportDto;
import com.sms.satp.entity.dto.InterfaceGroupDto;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.mapper.ApiInterfaceMapper;
import com.sms.satp.mapper.InterfaceGroupMapper;
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
import com.sms.satp.repository.InterfaceGroupRepository;
import com.sms.satp.service.ApiInterfaceService;
import com.sms.satp.utils.ApiHeaderConverter;
import com.sms.satp.utils.ApiParameterConverter;
import com.sms.satp.utils.ApiRequestBodyConverter;
import com.sms.satp.utils.ApiResponseConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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

    private static final Integer FIRST_TAG = 0;
    private static final String FILE_IMPORT = "file";
    private static final String URL_IMPORT = "url";

    private final ApiInterfaceRepository apiInterfaceRepository;
    private final InterfaceGroupRepository interfaceGroupRepository;
    private final DocumentFactory documentFactory;
    private final ApiInterfaceMapper apiInterfaceMapper;
    private final InterfaceGroupMapper interfaceGroupMapper;

    public ApiInterfaceServiceImpl(ApiInterfaceRepository apiInterfaceRepository,
        DocumentFactory documentFactory, ApiInterfaceMapper apiInterfaceMapper,
        InterfaceGroupRepository interfaceGroupRepository, InterfaceGroupMapper interfaceGroupMapper) {
        this.apiInterfaceRepository = apiInterfaceRepository;
        this.interfaceGroupRepository = interfaceGroupRepository;
        this.documentFactory = documentFactory;
        this.apiInterfaceMapper = apiInterfaceMapper;
        this.interfaceGroupMapper = interfaceGroupMapper;
    }

    @Override
    public Page<ApiInterfaceDto> page(PageDto pageDto, String projectId, String groupId) {
        try {
            ApiInterfaceBuilder apiInterfaceBuilder = ApiInterface.builder()
                .projectId(projectId);
            if (StringUtils.isNoneBlank(groupId)) {
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
    public void importByFile(MultipartFile multipartFile, String documentType, String projectId) throws IOException {
        log.info(
            "ApiInterfaceService-save()-params: [documentType]={}, [projectId]={}",
            documentType, projectId);
        String contents = IOUtils.toString(multipartFile.getInputStream(), StandardCharsets.UTF_8);
        save(FILE_IMPORT, contents, documentType, projectId);

    }

    @Override
    public void importByUrl(DataImportDto dataImportDto) {
        log.info(
            "ApiInterfaceService-saveByUrl()-params: [URL]={}, [documentType]={}, [projectId]={}",
            dataImportDto.getUrl(), dataImportDto.getType(), dataImportDto.getProjectId());
        save(URL_IMPORT, dataImportDto.getUrl(),
            dataImportDto.getType(), dataImportDto.getProjectId());
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
    public List<InterfaceGroupDto> getGroupList(String projectId) {
        try {
            InterfaceGroup interfaceGroup = InterfaceGroup.builder().projectId(projectId).build();
            Example<InterfaceGroup> example = Example.of(interfaceGroup);
            return interfaceGroupMapper.toDtoList(interfaceGroupRepository.findAll(example));
        } catch (Exception e) {
            log.error("Failed to get the InterfaceGroup list!", e);
            throw new ApiTestPlatformException(GET_INTERFACE_GROUP_LIST_ERROR);
        }
    }

    @Override
    public String addGroup(InterfaceGroupDto interfaceGroupDto) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("ApiInterfaceService-addGroup()-Parameter: %s",
                interfaceGroupDto.toString()));
        }
        try {
            InterfaceGroup interfaceGroup = interfaceGroupMapper.toEntity(interfaceGroupDto);
            interfaceGroup.setId(new ObjectId().toString());
            interfaceGroupRepository.insert(interfaceGroup);
            return interfaceGroup.getId();
        } catch (Exception e) {
            log.error("Failed to add the InterfaceGroup!", e);
            throw new ApiTestPlatformException(ADD_INTERFACE_GROUP_ERROR);
        }
    }

    @Override
    public void editGroup(InterfaceGroupDto interfaceGroupDto) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("ApiInterfaceService-editGroup()-Parameter: %s",
                interfaceGroupDto.toString()));
        }
        try {
            InterfaceGroup interfaceGroup = interfaceGroupMapper.toEntity(interfaceGroupDto);
            Optional<InterfaceGroup> interfaceGroupOptional = interfaceGroupRepository
                .findById(interfaceGroup.getId());
            if (interfaceGroupOptional.isPresent()) {
                interfaceGroupRepository.save(interfaceGroup);
            }
        } catch (Exception e) {
            log.error("Failed to add the InterfaceGroup!", e);
            throw new ApiTestPlatformException(EDIT_INTERFACE_GROUP_ERROR);
        }
    }

    @Override
    public void deleteGroup(String id) {
        try {
            interfaceGroupRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Failed to delete the InterfaceGroup!", e);
            throw new ApiTestPlatformException(DELETE_INTERFACE_GROUP_BY_ID_ERROR);
        }
    }

    @Override
    public String addGroupByNameAndReturnId(String groupName, String projectId) {
        Example<InterfaceGroup> example = Example
            .of(InterfaceGroup.builder().name(groupName).build());
        Optional<InterfaceGroup> interfaceGroupOptional = interfaceGroupRepository.findOne(example);
        if (interfaceGroupOptional.isPresent()) {
            return interfaceGroupOptional.get().getId();
        } else {
            return addGroup(InterfaceGroupDto.builder().name(groupName).projectId(projectId).build());
        }
    }

    private void save(String type, String contentsOrResource, String documentType, String projectId) {
        Optional<DocumentType> documentTypeOptional = Optional.ofNullable(
            DocumentType.resolve(documentType.toUpperCase(Locale.getDefault())));
        if (documentTypeOptional.isPresent()) {
            try {
                ApiDocument apiDocument = StringUtils.equals(type, FILE_IMPORT)
                    ?   documentFactory.buildByContents(contentsOrResource, documentTypeOptional.get()) :
                    documentFactory.buildByResource(contentsOrResource, documentTypeOptional.get());
                List<ApiInterface> apiInterfaces = convertApiPathsToApiInterfaces(
                    apiDocument, projectId);
                apiInterfaceRepository.insert(apiInterfaces);
            } catch (Exception e) {
                log.error("Failed to parse the file or url and save as ApiInterface!", e);
                throw new ApiTestPlatformException(PARSE_TO_APIINTERFACE_ERROR);
            }
        } else {
            throw new ApiTestPlatformException(DOCUMENT_TYPE_ERROR);
        }
    }


    private List<ApiInterface> convertApiPathsToApiInterfaces(ApiDocument apiDocument, String projectId) {
        List<ApiInterface> apiInterfaces = new ArrayList<>();
        List<ApiPath> apiPaths = apiDocument.getPaths();
        Map<String, ApiSchema> apiSchema = apiDocument.getSchemas();
        removeSchemaMapRef(apiSchema, apiSchema);
        apiPaths.forEach(apiPath ->
            apiPath.getOperations().forEach(apiOperation ->
                apiInterfaces.add(
                    apiPathOperationResolver(apiOperation, apiSchema, apiPath, projectId))
            )
        );
        return apiInterfaces;
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
        return tags.isEmpty() ? null : addGroupByNameAndReturnId(
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