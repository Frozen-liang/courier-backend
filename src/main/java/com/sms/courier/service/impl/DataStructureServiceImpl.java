package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.DATA_STRUCTURE;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.exception.ErrorCode.ADD_DATA_STRUCTURE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.CIRCULAR_REFERENCE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DATE_STRUCTURE_CANNOT_DELETE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_DATA_STRUCTURE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_DATA_STRUCTURE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_DATA_STRUCTURE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_DATA_STRUCTURE_DATA_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_DATA_STRUCTURE_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_DATA_STRUCTURE_REF_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_NAME_EXISTS_ERROR;
import static com.sms.courier.common.field.CommonField.CREATE_USER_ID;
import static com.sms.courier.common.field.CommonField.ID;
import static com.sms.courier.common.field.DataStructureField.DESCRIPTION;
import static com.sms.courier.common.field.DataStructureField.NAME;
import static com.sms.courier.common.field.DataStructureField.REF_ID;
import static com.sms.courier.common.field.DataStructureField.REF_STRUCT_IDS;
import static com.sms.courier.common.field.DataStructureField.STRUCT;
import static com.sms.courier.common.field.DataStructureField.STRUCTURE_REF;
import static com.sms.courier.common.field.DataStructureField.STRUCT_TYPE;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.DataStructureListRequest;
import com.sms.courier.dto.request.DataStructureRequest;
import com.sms.courier.dto.response.DataStructureListResponse;
import com.sms.courier.dto.response.DataStructureReferenceResponse;
import com.sms.courier.dto.response.DataStructureResponse;
import com.sms.courier.entity.api.common.ParamInfo;
import com.sms.courier.entity.structure.StructureEntity;
import com.sms.courier.entity.structure.StructureRefRecordEntity;
import com.sms.courier.mapper.DataStructureMapper;
import com.sms.courier.repository.ApiDataStructureRefRecordRepository;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.DataStructureRefRecordRepository;
import com.sms.courier.repository.DataStructureRepository;
import com.sms.courier.service.DataStructureService;
import com.sms.courier.utils.Assert;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.IdUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DataStructureServiceImpl implements DataStructureService {

    private final DataStructureRepository dataStructureRepository;
    private final DataStructureRefRecordRepository structureRefRecordRepository;
    private final ApiDataStructureRefRecordRepository apiDataStructureRefRecordRepository;
    private final CommonRepository commonRepository;
    private final DataStructureMapper dataStructureMapper;

    public DataStructureServiceImpl(DataStructureRepository dataStructureRepository,
        DataStructureRefRecordRepository structureRefRecordRepository,
        ApiDataStructureRefRecordRepository apiDataStructureRefRecordRepository,
        CommonRepository commonRepository,
        DataStructureMapper dataStructureMapper) {
        this.dataStructureRepository = dataStructureRepository;
        this.structureRefRecordRepository = structureRefRecordRepository;
        this.apiDataStructureRefRecordRepository = apiDataStructureRefRecordRepository;
        this.commonRepository = commonRepository;
        this.dataStructureMapper = dataStructureMapper;
    }

    @Override
    public DataStructureResponse findById(String id) {
        return dataStructureRepository.findById(id).map(dataStructureMapper::toResponse)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_DATA_STRUCTURE_BY_ID_ERROR));
    }


    @Override
    @LogRecord(operationType = ADD, operationModule = DATA_STRUCTURE,
        template = "{{#dataStructureRequest.name}}", refId = "refId")
    public Boolean add(DataStructureRequest dataStructureRequest) {
        log.info("DataStructureService-add()-params: [DataStructure]={}", dataStructureRequest.toString());
        try {
            StructureEntity dataStructure = dataStructureMapper.toEntity(dataStructureRequest);
            setParamId(dataStructure.getStruct());
            dataStructureRepository.insert(dataStructure);
            saveRef(dataStructure.getId(), dataStructure.getName(), dataStructureRequest.getAddStructIds(),
                dataStructureRequest.getRemoveStructIds());
        } catch (DuplicateKeyException e) {
            log.error("Failed to add the DataStructure!", e);
            throw ExceptionUtils.mpe(THE_NAME_EXISTS_ERROR, dataStructureRequest.getName());
        } catch (Exception e) {
            log.error("Failed to add the DataStructure!", e);
            throw new ApiTestPlatformException(ADD_DATA_STRUCTURE_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = DATA_STRUCTURE,
        template = "{{#dataStructureRequest.name}}", refId = "refId")
    public Boolean edit(DataStructureRequest dataStructureRequest) {
        log.info("DataStructureService-edit()-params: [DataStructure]={}", dataStructureRequest.toString());
        try {
            String id = dataStructureRequest.getId();
            boolean exists = dataStructureRepository.existsById(id);
            if (!exists) {
                throw ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "DataStructure", dataStructureRequest.getId());
            }
            List<String> addStructIds = dataStructureRequest.getAddStructIds();
            List<String> removeStructIds = dataStructureRequest.getRemoveStructIds();
            // Check for circular references.
            checkRefIds(addStructIds, removeStructIds, id);
            StructureEntity dataStructure = dataStructureMapper.toEntity(dataStructureRequest);
            setParamId(dataStructure.getStruct());
            dataStructureRepository.save(dataStructure);
            saveRef(dataStructure.getId(), dataStructure.getName(), addStructIds, removeStructIds);
        } catch (ApiTestPlatformException courierException) {
            log.error(courierException.getMessage());
            throw courierException;
        } catch (DuplicateKeyException e) {
            log.error("Failed to add the DataStructure!", e);
            throw ExceptionUtils.mpe(THE_NAME_EXISTS_ERROR, dataStructureRequest.getName());
        } catch (Exception e) {
            log.error("Failed to add the DataStructure!", e);
            throw new ApiTestPlatformException(EDIT_DATA_STRUCTURE_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = DATA_STRUCTURE,
        template = "{{#result.name}}",
        enhance = @Enhance(enable = true), refId = "refId")
    public Boolean delete(String id) {
        try {
            List<StructureRefRecordEntity> refRecord = getRefRecord(id);
            Assert.isTrue(CollectionUtils.isEmpty(refRecord), DATE_STRUCTURE_CANNOT_DELETE_ERROR, "data structure");
            List<DataStructureReferenceResponse> apiRefs = apiDataStructureRefRecordRepository
                .findByRefStructIdsIs(id);
            Assert.isTrue(CollectionUtils.isEmpty(apiRefs), DATE_STRUCTURE_CANNOT_DELETE_ERROR, "api");
            structureRefRecordRepository.deleteById(id);
            return dataStructureRepository.deleteByIdIs(id) > 0;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to delete the DataStructure!", e);
            throw new ApiTestPlatformException(DELETE_DATA_STRUCTURE_BY_ID_ERROR);
        }
    }

    /**
     * 查询数据结构列表.
     */
    @Override
    public List<DataStructureListResponse> getDataStructureList(DataStructureListRequest request) {
        try {
            Query query = new Query();
            query.fields().exclude(STRUCT.getName(), REF_STRUCT_IDS.getName());
            NAME.like(request.getName()).ifPresent(query::addCriteria);
            REF_ID.in(getIds(request.getProjectId(), request.getWorkspaceId())).ifPresent(query::addCriteria);
            DESCRIPTION.like(request.getDescription()).ifPresent(query::addCriteria);
            STRUCT_TYPE.is(request.getStructType()).ifPresent(query::addCriteria);
            return commonRepository.list(query, StructureEntity.class).stream()
                .map(dataStructureMapper::toListResponse)
                .peek(response -> response.setQuoted(structureRefRecordRepository.existsByStructureRef(
                    StructureRefRecordEntity.builder().id(response.getId()).build())
                    || apiDataStructureRefRecordRepository.existsByRefStructIdsIs(response.getId())))
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to get the DataStructure list!", e);
            throw new ApiTestPlatformException(GET_DATA_STRUCTURE_LIST_ERROR);
        }
    }

    /**
     * 查询api可以引用的数据结构列表.
     */
    @Override
    public List<DataStructureResponse> getDataStructureDataList(DataStructureListRequest request) {
        try {
            Query query = new Query();
            query.fields().exclude(CREATE_USER_ID.getName());
            // ID.ne(request.getId()).ifPresent(query::addCriteria);
            STRUCT_TYPE.is(request.getStructType()).ifPresent(query::addCriteria);
            REF_ID.in(getIds(request.getProjectId(), request.getWorkspaceId())).ifPresent(query::addCriteria);
            // REF_STRUCT_IDS.ne(request.getId()).ifPresent(query::addCriteria);
            return dataStructureMapper.toResponseList(commonRepository.list(query, StructureEntity.class));
        } catch (Exception e) {
            log.error("Failed to get the DataStructure data list!", e);
            throw new ApiTestPlatformException(GET_DATA_STRUCTURE_DATA_LIST_ERROR);
        }
    }


    /**
     * 查询当前数据结构引用信息.
     */
    @Override
    public Map<String, List<DataStructureReferenceResponse>> getReference(String id) {
        List<StructureRefRecordEntity> refRecords = getRefRecord(id);
        Map<String, List<DataStructureReferenceResponse>> result = new HashMap<>();
        result.put("api", apiDataStructureRefRecordRepository.findByRefStructIdsIs(id));
        result.put("dataStructure", dataStructureMapper.toReferenceResponses(refRecords));
        return result;
    }

    /**
     * 查询当前数据结构可引用的数据结构.
     */
    @Override
    public List<DataStructureResponse> getRefStructList(DataStructureListRequest request) {
        try {
            String id = request.getId();
            List<String> refIds = getIds(request.getProjectId(), request.getWorkspaceId());
            //id为空 则为新建数据结构时查询其可引用的
            if (StringUtils.isBlank(id)) {
                return dataStructureRepository.findByRefIdInAndStructType(refIds, request.getStructType());
            }
            Set<String> ids = getRefRecord(request.getId()).stream().map(StructureRefRecordEntity::getId)
                .collect(Collectors.toSet());
            ids.add(id);
            return dataStructureRepository.findByRefIdInAndStructTypeAndIdNotIn(refIds, request.getStructType(), ids);
        } catch (Exception e) {
            log.error("Failed to get the ref list !", e);
            throw new ApiTestPlatformException(GET_DATA_STRUCTURE_REF_LIST_ERROR);
        }
    }


    private void setParamId(List<ParamInfo> struct) {
        if (CollectionUtils.isEmpty(struct)) {
            return;
        }
        for (ParamInfo paramInfo : struct) {
            if (StringUtils.isBlank(paramInfo.getParamId())) {
                paramInfo.setParamId(String.valueOf(IdUtil.generatorId()));
            }
            setParamId(paramInfo.getChildParam());
        }
    }

    private void checkRefIds(List<String> addStructIds, List<String> removeStructIds, String id) {
        addStructIds = Objects.requireNonNullElse(addStructIds, new ArrayList<>());
        removeStructIds = Objects.requireNonNullElse(removeStructIds, new ArrayList<>());
        // 获取新增和删除的交集
        Collection<String> intersection = CollectionUtils.intersection(addStructIds, removeStructIds);

        // 删除交集中的数据
        if (CollectionUtils.isNotEmpty(intersection)) {
            intersection.forEach(addStructIds::remove);
            intersection.forEach(removeStructIds::remove);
        }
        if (!addStructIds.isEmpty()) {
            List<StructureRefRecordEntity> refEntities = structureRefRecordRepository.findByIdIn(addStructIds);
            checkRefEntity(refEntities, id);
        }
    }

    private void checkRefEntity(List<StructureRefRecordEntity> refEntities, String id) {
        if (CollectionUtils.isEmpty(refEntities)) {
            return;
        }
        for (StructureRefRecordEntity refRecordEntity : refEntities) {
            // 如果当前数据结构直接引用或间接引用中包含当前结构 则出现了循环引用 抛出异常
            if (id.equals(refRecordEntity.getId())) {
                throw ExceptionUtils.mpe(CIRCULAR_REFERENCE_ERROR);
            }
            checkRefEntity(refRecordEntity.getStructureRef(), id);
        }
    }

    private void saveRef(String id, String name, List<String> addStructIds, List<String> removeStructIds) {
        addStructIds = Objects.requireNonNullElse(addStructIds, new ArrayList<>());
        removeStructIds = Objects.requireNonNullElse(removeStructIds, new ArrayList<>());
        StructureRefRecordEntity structureRefRecordEntity = structureRefRecordRepository.findById(id)
            .orElse(StructureRefRecordEntity.builder().id(id).structureRef(new ArrayList<>()).build());
        List<StructureRefRecordEntity> structureRef = structureRefRecordEntity.getStructureRef();
        // 增加新增的引用
        addStructIds.forEach((refId) -> structureRef.add(StructureRefRecordEntity.builder().id(refId).build()));
        // 去掉减少的引用
        removeStructIds.forEach(refId -> structureRef.remove(StructureRefRecordEntity.builder().id(refId).build()));
        structureRefRecordEntity.setName(name);
        structureRefRecordRepository.save(structureRefRecordEntity);
    }

    private List<StructureRefRecordEntity> getRefRecord(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where(STRUCTURE_REF.getName())
            .is(StructureRefRecordEntity.builder().id(id).build()));
        query.fields().include(ID.getName(), NAME.getName());
        return commonRepository.list(query, StructureRefRecordEntity.class);
    }

    private List<String> getIds(String projectId, String workspaceId) {
        List<String> ids = new ArrayList<>();
        if (StringUtils.isNotBlank(projectId)) {
            ids.add(projectId);
        }
        if (StringUtils.isNotBlank(workspaceId)) {
            ids.add(workspaceId);
        }
        return ids;
    }
}
