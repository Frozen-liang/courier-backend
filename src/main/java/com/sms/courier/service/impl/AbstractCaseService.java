package com.sms.courier.service.impl;

import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.dto.response.ParamInfoResponse;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.api.common.ParamInfo;
import com.sms.courier.mapper.ParamInfoMapper;
import com.sms.courier.repository.ApiRepository;
import com.sms.courier.utils.DataStructureUtil;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.MD5Util;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

public abstract class AbstractCaseService {

    private final ApiRepository apiRepository;
    private final ParamInfoMapper paramInfoMapper;

    protected AbstractCaseService(ApiRepository apiRepository, ParamInfoMapper paramInfoMapper) {
        this.apiRepository = apiRepository;
        this.paramInfoMapper = paramInfoMapper;
    }

    protected void syncApiEntity(ApiEntity targetApiEntity) {
        ApiEntity sourceApiEntity = apiRepository.findById(targetApiEntity.getId())
            .orElseThrow(() -> ExceptionUtils.mpe(ErrorCode.GET_API_BY_ID_ERROR));
        targetApiEntity.setApiPath(sourceApiEntity.getApiPath());
        targetApiEntity.setRequestMethod(sourceApiEntity.getRequestMethod());
        targetApiEntity.setApiRequestParamType(sourceApiEntity.getApiRequestParamType());
        targetApiEntity.setApiResponseParamType(sourceApiEntity.getApiResponseParamType());
        targetApiEntity.setApiRequestJsonType(sourceApiEntity.getApiRequestJsonType());
        targetApiEntity.setApiResponseJsonType(sourceApiEntity.getApiResponseJsonType());
        targetApiEntity.setRequestHeaders(syncParam(sourceApiEntity.getRequestHeaders(),
            targetApiEntity.getRequestHeaders()));
        targetApiEntity.setResponseHeaders(syncParam(sourceApiEntity.getResponseHeaders(),
            targetApiEntity.getResponseHeaders()));
        targetApiEntity.setPathParams(syncParam(sourceApiEntity.getPathParams(), targetApiEntity.getPathParams()));
        targetApiEntity.setRestfulParams(syncParam(sourceApiEntity.getRestfulParams(),
            targetApiEntity.getRestfulParams()));
        targetApiEntity.setRequestParams(syncDataStructureParam(sourceApiEntity.getRequestParams(),
            targetApiEntity.getRequestParams()));
        targetApiEntity.setResponseParams(syncDataStructureParam(sourceApiEntity.getResponseParams(),
            targetApiEntity.getResponseParams()));

    }

    private List<ParamInfo> syncDataStructureParam(List<ParamInfo> sourceInfoList, List<ParamInfo> targetInfoList) {
        if (CollectionUtils.isNotEmpty(sourceInfoList)) {
            boolean isContainsDataStructure = sourceInfoList.stream().anyMatch(ParamInfo::isRef);
            if (isContainsDataStructure) {
                List<ParamInfoResponse> paramInfoResponseList = paramInfoMapper.toDtoList(sourceInfoList);
                List<ParamInfoResponse> source = DataStructureUtil.resetApiRequest(paramInfoResponseList);
                sourceInfoList = paramInfoMapper.toEntityByResponseList(source);
            }
            return syncParam(sourceInfoList, targetInfoList);
        }
        return targetInfoList;
    }

    private List<ParamInfo> syncParam(List<ParamInfo> sourceInfoList, List<ParamInfo> targetInfoList) {
        if (CollectionUtils.isNotEmpty(sourceInfoList)) {
            if (CollectionUtils.isNotEmpty(targetInfoList)
                && !Objects.equals(MD5Util.getMD5(sourceInfoList), MD5Util.getMD5(targetInfoList))) {
                return syncPramInfo(sourceInfoList, targetInfoList);
            }
            if (CollectionUtils.isEmpty(targetInfoList)) {
                targetInfoList = sourceInfoList;
                return targetInfoList;
            }
        }
        return targetInfoList;
    }

    private List<ParamInfo> syncPramInfo(List<ParamInfo> sourceInfoList, List<ParamInfo> targetInfoList) {
        Map<String, ParamInfo> sourceInfoMap = sourceInfoList.stream().collect(Collectors.toMap(ParamInfo::getKey,
            Function.identity()));

        for (ParamInfo paramInfo : targetInfoList) {
            if (sourceInfoMap.containsKey(paramInfo.getKey())) {
                ParamInfo sourceParam = sourceInfoMap.get(paramInfo.getKey());
                if (Objects.equals(paramInfo.getParamType(), sourceParam.getParamType())
                    && CollectionUtils.isNotEmpty(paramInfo.getChildParam())) {
                    syncPramInfo(sourceInfoMap.get(paramInfo.getKey()).getChildParam(), paramInfo.getChildParam());
                }
                if (!Objects.equals(paramInfo.getParamType(), sourceParam.getParamType())) {
                    paramInfo.setParamType(sourceParam.getParamType());
                    paramInfo.setChildParam(sourceParam.getChildParam());
                }

            }
        }
        Map<String, ParamInfo> targetInfoMap = targetInfoList.stream().collect(Collectors.toMap(ParamInfo::getKey,
            Function.identity()));
        List<ParamInfo> addParamList = sourceInfoList.stream()
            .filter(source -> !targetInfoMap.containsKey(source.getKey()))
            .collect(Collectors.toList());
        targetInfoList.addAll(addParamList);
        return targetInfoList;
    }

}
