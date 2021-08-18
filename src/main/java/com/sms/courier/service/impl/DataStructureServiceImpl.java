package com.sms.courier.service.impl;


import static com.sms.courier.common.exception.ErrorCode.ADD_DATA_STRUCTURE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_DATA_STRUCTURE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_DATA_STRUCTURE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_DATA_STRUCTURE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_DATA_STRUCTURE_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_NAME_EXISTS_ERROR;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.DataStructureRequest;
import com.sms.courier.dto.response.DataStructureResponse;
import com.sms.courier.entity.api.common.ParamInfo;
import com.sms.courier.entity.structure.StructureEntity;
import com.sms.courier.mapper.DataStructureMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.DataStructureRepository;
import com.sms.courier.service.DataStructureService;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.IdUtil;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.dao.DuplicateKeyException;
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
    public List<DataStructureResponse> list() {
        try {
            return null;
//            return dataStructureMapper.toResponseList(dataStructureRepository.findAll(example, sort));
        } catch (Exception e) {
            log.error("Failed to get the DataStructure list! message:{}", e.getMessage());
            throw new ApiTestPlatformException(GET_DATA_STRUCTURE_LIST_ERROR);
        }
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

    private void setParamId(List<ParamInfo> struct) {
        if (CollectionUtils.isEmpty(struct)) {
            return;
        }
        for (ParamInfo paramInfo : struct) {
            paramInfo.setParamId(IdUtil.generatorId());
            setParamId(paramInfo.getChildParam());
        }
    }
}
