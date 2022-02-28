package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.AWS3_SETTING;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.exception.ErrorCode.DELETE_AWS3_SETTING_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_AWS3_SETTING_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.QUERY_AWS3_SETTING_API_ERROR;
import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.AmazonStorageSettingRequest;
import com.sms.courier.dto.response.AmazonStorageSettingResponse;
import com.sms.courier.entity.file.AmazonStorageSettingEntity;
import com.sms.courier.mapper.AmazonStorageSettingMapper;
import com.sms.courier.repository.AmazonStorageSettingRepository;
import com.sms.courier.service.AmazonStorageSettingService;
import com.sms.courier.storagestrategy.strategy.impl.AmazonStorageService;
import com.sms.courier.utils.AesUtil;
import com.sms.courier.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AmazonStorageSettingServiceImpl implements AmazonStorageSettingService {

    private final AmazonStorageSettingRepository amazonStorageSettingRepository;
    private final AmazonStorageSettingMapper amazonStorageSettingMapper;
    private final AmazonStorageService amazonStorageService;

    public AmazonStorageSettingServiceImpl(AmazonStorageSettingRepository amazonStorageSettingRepository,
                                           AmazonStorageSettingMapper amazonStorageSettingMapper,
                                           AmazonStorageService amazonStorageService) {
        this.amazonStorageSettingRepository = amazonStorageSettingRepository;
        this.amazonStorageSettingMapper = amazonStorageSettingMapper;
        this.amazonStorageService = amazonStorageService;
    }

    @Override
    public AmazonStorageSettingResponse findOne() {
        try {
            return amazonStorageSettingRepository.findFirstByOrderByModifyDateTime()
                    .orElse(new AmazonStorageSettingResponse());
        } catch (Exception e) {
            log.error("Failed to edit the Amazon Storage Setting!", e);
            throw ExceptionUtils.mpe(QUERY_AWS3_SETTING_API_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = AWS3_SETTING,
            template = "{{#amazonStorageSettingRequest.bucketName}}")
    public Boolean add(AmazonStorageSettingRequest amazonStorageSettingRequest) {
        AmazonStorageSettingEntity amazonStorage = amazonStorageSettingMapper.toEntity(amazonStorageSettingRequest);
        amazonStorage.setRemoved(amazonStorageSettingRequest.isRemoved());
        amazonStorage.setAccessKeyId(AesUtil.encrypt(amazonStorage.getAccessKeyId()));
        amazonStorage.setAccessKeySecret(AesUtil.encrypt(amazonStorage.getAccessKeySecret()));
        AmazonStorageSettingEntity amazonStorageSetting = amazonStorageSettingRepository.insert(amazonStorage);
        amazonStorageService.createS3Client(amazonStorageSetting);
        return true;
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = AWS3_SETTING,
            template = "{{#amazonStorageSettingRequest.bucketName}}")
    public Boolean edit(AmazonStorageSettingRequest amazonStorageSettingRequest) {
        log.info("AmazonStorageService-edit()-params: [AmazonStorage]={}", amazonStorageSettingRequest.toString());
        try {
            AmazonStorageSettingEntity amazonStorage = amazonStorageSettingMapper.toEntity(amazonStorageSettingRequest);
            amazonStorage.setRemoved(amazonStorageSettingRequest.isRemoved());
            String id = amazonStorage.getId();
            AmazonStorageSettingEntity oldAmazonStorage =
                    amazonStorageSettingRepository.findById(id).orElseThrow(() -> ExceptionUtils
                            .mpe(EDIT_NOT_EXIST_ERROR, "AmazonStorage", id));
            amazonStorage
                    .setAccessKeyId(StringUtils.isNotBlank(amazonStorage.getAccessKeyId())
                            ? AesUtil.encrypt(amazonStorage.getAccessKeyId()) : oldAmazonStorage.getAccessKeyId());
            amazonStorage
                    .setAccessKeySecret(StringUtils.isNotBlank(amazonStorage.getAccessKeySecret())
                            ? AesUtil.encrypt(amazonStorage.getAccessKeySecret()) :
                            oldAmazonStorage.getAccessKeySecret());
            amazonStorage = amazonStorageSettingRepository.save(amazonStorage);
            amazonStorageService.createS3Client(amazonStorage);
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (Exception e) {
            log.error("Failed to edit the AmazonStorage!", e);
            throw new ApiTestPlatformException(EDIT_AWS3_SETTING_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = AWS3_SETTING,
            template = "{{#result?.![#this.bucketName]}}",
            enhance = @Enhance(enable = true))
    public Boolean delete(String id) {
        try {
            amazonStorageSettingRepository.deleteById(id);
            amazonStorageService.createS3Client(null);
            return true;
        } catch (Exception e) {
            log.error("Failed to edit the AmazonStorage!", e);
            throw new ApiTestPlatformException(DELETE_AWS3_SETTING_BY_ID_ERROR);
        }
    }

}
