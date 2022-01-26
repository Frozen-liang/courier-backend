package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.OPEN_API_SETTING;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.exception.ErrorCode.ADD_OPEN_API_SETTING_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_OPEN_API_SETTING_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_OPEN_API_SETTING_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_OPEN_API_SETTING_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_OPEN_API_SETTING_LIST_ERROR;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.enums.OperationModule;
import com.sms.courier.common.enums.OperationType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.OpenApiSettingRequest;
import com.sms.courier.dto.response.OpenApiSettingResponse;
import com.sms.courier.entity.openapi.OpenApiSettingEntity;
import com.sms.courier.mapper.OpenApiSettingMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.OpenApiSettingRepository;
import com.sms.courier.security.jwt.JwtTokenManager;
import com.sms.courier.security.pojo.CustomUser;
import com.sms.courier.service.OpenApiSettingService;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.SecurityUtil;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OpenApiSettingServiceImpl implements OpenApiSettingService {

    private final OpenApiSettingRepository openApiSettingRepository;
    private final CommonRepository commonRepository;
    private final JwtTokenManager jwtTokenManager;
    private final OpenApiSettingMapper openApiSettingMapper;

    public OpenApiSettingServiceImpl(OpenApiSettingRepository openApiSettingRepository,
        CommonRepository commonRepository,
        JwtTokenManager jwtTokenManager, OpenApiSettingMapper openApiSettingMapper) {
        this.openApiSettingRepository = openApiSettingRepository;
        this.commonRepository = commonRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.openApiSettingMapper = openApiSettingMapper;
    }

    @Override
    public OpenApiSettingResponse findById(String id) {
        return openApiSettingRepository.findById(id).map(openApiSettingMapper::toDto)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_OPEN_API_SETTING_BY_ID_ERROR));
    }

    @Override
    public List<OpenApiSettingResponse> list() {
        try {
            return openApiSettingMapper.toDtoList(openApiSettingRepository.findAll());
        } catch (Exception e) {
            log.error("Failed to get the OpenApiSetting list!", e);
            throw new ApiTestPlatformException(GET_OPEN_API_SETTING_LIST_ERROR);
        }
    }


    @Override
    @LogRecord(operationType = ADD, operationModule = OPEN_API_SETTING, template = "{{#openApiSettingRequest.name}}")
    public Boolean add(OpenApiSettingRequest openApiSettingRequest) {
        log.info("OpenApiSettingService-add()-params: [OpenApiSetting]={}", openApiSettingRequest.toString());
        try {
            String id = ObjectId.get().toString();
            OpenApiSettingEntity openApiSetting = openApiSettingMapper.toEntity(openApiSettingRequest);
            String token = jwtTokenManager.generateAccessToken(CustomUser.createOpenApi(id, openApiSetting.getName()));
            openApiSetting.setId(id);
            openApiSetting.setCreateDateTime(LocalDateTime.now());
            openApiSetting.setCreateUserId(SecurityUtil.getCurrUserId());
            openApiSetting.setToken(token);
            openApiSettingRepository.insert(openApiSetting);
        } catch (DuplicateKeyException e) {
            String message = "The " + openApiSettingRequest.getName() + " exits!";
            log.error(message, e);
            throw ExceptionUtils.mpe(message);
        } catch (Exception e) {
            log.error("Failed to add the OpenApiSetting!", e);
            throw new ApiTestPlatformException(ADD_OPEN_API_SETTING_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = OPEN_API_SETTING, template = "{{#openApiSettingRequest.name}}")
    public Boolean edit(OpenApiSettingRequest openApiSettingRequest) {
        log.info("OpenApiSettingService-edit()-params: [OpenApiSetting]={}", openApiSettingRequest.toString());
        try {
            OpenApiSettingEntity oldOpenApiSetting = openApiSettingRepository.findById(openApiSettingRequest.getId())
                .orElseThrow(
                    () -> ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "OpenApiSetting", openApiSettingRequest.getId()));
            OpenApiSettingEntity openApiSetting = openApiSettingMapper.toEntity(openApiSettingRequest);
            openApiSetting.setToken(oldOpenApiSetting.getToken());
            openApiSettingRepository.save(openApiSetting);
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (DuplicateKeyException e) {
            String message = "The " + openApiSettingRequest.getName() + " exits!";
            log.error(message, e);
            throw ExceptionUtils.mpe(message);
        } catch (Exception e) {
            log.error("Failed to add the OpenApiSetting!", e);
            throw new ApiTestPlatformException(EDIT_OPEN_API_SETTING_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = OPEN_API_SETTING,
        template = "{{#result?.![#this.name]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean delete(List<String> ids) {
        try {
            return openApiSettingRepository.deleteByIdIn(ids) > 0;
        } catch (Exception e) {
            log.error("Failed to delete the OpenApiSetting!", e);
            throw new ApiTestPlatformException(DELETE_OPEN_API_SETTING_BY_ID_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = OperationType.UNLOCK, operationModule = OperationModule.OPEN_API_SETTING,
        template = "{{#result?.![#this.name]}}", enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean enable(List<String> ids) {
        return commonRepository.recover(ids, OpenApiSettingEntity.class);
    }

    @Override
    @LogRecord(operationType = OperationType.LOCK, operationModule = OPEN_API_SETTING,
        template = "{{#result?.![#this.name]}}", enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean unable(List<String> ids) {
        return commonRepository.deleteByIds(ids, OpenApiSettingEntity.class);
    }


}
