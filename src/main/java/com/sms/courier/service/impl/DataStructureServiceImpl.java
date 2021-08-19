package com.sms.courier.service.impl;


import static com.sms.courier.common.exception.ErrorCode.ADD_DATA_STRUCTURE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_DATA_STRUCTURE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_DATA_STRUCTURE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_DATA_STRUCTURE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_DATA_STRUCTURE_DATA_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_DATA_STRUCTURE_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_NAME_EXISTS_ERROR;
import static com.sms.courier.common.field.CommonField.CREATE_USER_ID;
import static com.sms.courier.common.field.CommonField.ID;
import static com.sms.courier.common.field.DataStructureField.DESCRIPTION;
import static com.sms.courier.common.field.DataStructureField.NAME;
import static com.sms.courier.common.field.DataStructureField.REF_ID;
import static com.sms.courier.common.field.DataStructureField.REF_STRUCT_IDS;
import static com.sms.courier.common.field.DataStructureField.STRUCT;
import static com.sms.courier.common.field.DataStructureField.STRUCT_TYPE;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.DataStructureListRequest;
import com.sms.courier.dto.request.DataStructureRequest;
import com.sms.courier.dto.response.DataStructureListResponse;
import com.sms.courier.dto.response.DataStructureReferenceResponse;
import com.sms.courier.dto.response.DataStructureResponse;
import com.sms.courier.entity.api.common.ParamInfo;
import com.sms.courier.entity.structure.StructureEntity;
import com.sms.courier.mapper.DataStructureMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.DataStructureRepository;
import com.sms.courier.service.DataStructureService;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.IdUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DataStructureServiceImpl implements DataStructureService {

    private final DataStructureRepository dataStructureRepository;
    private final CommonRepository commonRepository;
    private final DataStructureMapper dataStructureMapper;

    public DataStructureServiceImpl(DataStructureRepository dataStructureRepository,
        CommonRepository commonRepository,
        DataStructureMapper dataStructureMapper) {
        this.dataStructureRepository = dataStructureRepository;
        this.commonRepository = commonRepository;
        this.dataStructureMapper = dataStructureMapper;
    }

    @Override
    public DataStructureResponse findById(String id) {
        return dataStructureRepository.findById(id).map(dataStructureMapper::toResponse)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_DATA_STRUCTURE_BY_ID_ERROR));
    }


    @Override
    public Boolean add(DataStructureRequest dataStructureRequest) {
        log.info("DataStructureService-add()-params: [DataStructure]={}", dataStructureRequest.toString());
        try {
            StructureEntity dataStructure = dataStructureMapper.toEntity(dataStructureRequest);
            setParamId(dataStructure.getStruct());
            dataStructureRepository.insert(dataStructure);
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
    public Boolean edit(DataStructureRequest dataStructureRequest) {
        log.info("DataStructureService-edit()-params: [DataStructure]={}", dataStructureRequest.toString());
        try {
            boolean exists = dataStructureRepository.existsById(dataStructureRequest.getId());
            if (!exists) {
                throw ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "DataStructure", dataStructureRequest.getId());
            }
            StructureEntity dataStructure = dataStructureMapper.toEntity(dataStructureRequest);
            dataStructureRepository.save(dataStructure);
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
    public Boolean delete(List<String> ids) {
        try {
            return dataStructureRepository.deleteByIdIn(ids);
        } catch (Exception e) {
            log.error("Failed to delete the DataStructure!", e);
            throw new ApiTestPlatformException(DELETE_DATA_STRUCTURE_BY_ID_ERROR);
        }
    }

    @Override
    public List<DataStructureListResponse> getDataStructureList(DataStructureListRequest request) {
        try {
            Query query = new Query();
            query.fields().exclude(STRUCT.getName(), REF_STRUCT_IDS.getName());
            NAME.like(request.getName()).ifPresent(query::addCriteria);
            REF_ID.in(getIds(request.getProjectId(), request.getWorkspaceId()));
            DESCRIPTION.like(request.getDescription()).ifPresent(query::addCriteria);
            STRUCT_TYPE.is(request.getStructType()).ifPresent(query::addCriteria);
            return commonRepository.list(query, StructureEntity.class).stream()
                .map(dataStructureMapper::toListResponse)
                .peek(response -> response.setQuoted(dataStructureRepository.existsByRefStructIds(response.getId())))
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to get the DataStructure list!", e);
            throw new ApiTestPlatformException(GET_DATA_STRUCTURE_LIST_ERROR);
        }
    }

    @Override
    public List<DataStructureResponse> getDataStructureDataList(DataStructureListRequest request) {
        try {
            Query query = new Query();
            query.fields().exclude(CREATE_USER_ID.getName());
            ID.ne(request.getId()).ifPresent(query::addCriteria);
            REF_ID.in(getIds(request.getProjectId(), request.getWorkspaceId())).ifPresent(query::addCriteria);
            REF_STRUCT_IDS.ne(request.getId()).ifPresent(query::addCriteria);
            return dataStructureMapper.toResponseList(commonRepository.list(query, StructureEntity.class));
        } catch (Exception e) {
            log.error("Failed to get the DataStructure data list!", e);
            throw new ApiTestPlatformException(GET_DATA_STRUCTURE_DATA_LIST_ERROR);
        }
    }

    @Override
    public List<DataStructureReferenceResponse> getReference(String id) {
        Query query = new Query();
        REF_STRUCT_IDS.is(id).ifPresent(query::addCriteria);
        query.fields().include(ID.getName(), NAME.getName());
        return dataStructureMapper.toReferenceResponse(commonRepository.list(query, StructureEntity.class));
    }

    private void setParamId(List<ParamInfo> struct) {
        if (CollectionUtils.isEmpty(struct)) {
            return;
        }
        for (ParamInfo paramInfo : struct) {
            paramInfo.setParamId(String.valueOf(IdUtil.generatorId()));
            setParamId(paramInfo.getChildParam());
        }
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
