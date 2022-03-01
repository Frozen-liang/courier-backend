package com.sms.courier.storagestrategy.strategy.impl;

import static com.sms.courier.common.exception.ErrorCode.CONFIG_AMAZON_SERVICE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_AMAZON_SERVICE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DOWNLOAD_AMAZON_SERVICE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.STORE_AMAZON_SERVICE_ERROR;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.sms.courier.entity.file.AmazonStorageSettingEntity;
import com.sms.courier.entity.file.FileInfoEntity;
import com.sms.courier.repository.AmazonStorageSettingRepository;
import com.sms.courier.storagestrategy.StorageType;
import com.sms.courier.storagestrategy.model.DownloadModel;
import com.sms.courier.storagestrategy.strategy.FileStorageService;
import com.sms.courier.storagestrategy.strategy.StorageStrategy;
import com.sms.courier.utils.AesUtil;
import com.sms.courier.utils.ExceptionUtils;
import java.io.IOException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.multipart.MultipartFile;

@StorageStrategy(type = StorageType.AMAZON)
@Slf4j
public class AmazonStorageService implements FileStorageService, InitializingBean {

    private final AmazonStorageSettingRepository amazonStorageSettingRepository;
    private AmazonS3 amazonS3;
    private String bucketName;

    public AmazonStorageService(AmazonStorageSettingRepository amazonStorageSettingRepository) {
        this.amazonStorageSettingRepository = amazonStorageSettingRepository;
    }

    @SneakyThrows(IOException.class)
    @Override
    public boolean store(FileInfoEntity fileInfo, MultipartFile file) {
        check();
        String key = fileInfo.getId() + "-" + file.getOriginalFilename();
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentLength(file.getSize());
            amazonS3.putObject(new PutObjectRequest(bucketName, key, file.getInputStream(), objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            fileInfo.setSourceId(key);
        } catch (AmazonServiceException e) {
            log.error("Failed to store the File!", e);
            throw ExceptionUtils.mpe(STORE_AMAZON_SERVICE_ERROR);
        }
        return Boolean.TRUE;
    }

    private void check() {
        if (amazonS3 == null) {
            log.error("The amazonS3 not config!");
            throw ExceptionUtils.mpe(CONFIG_AMAZON_SERVICE_ERROR);
        }
    }

    @Override
    public Boolean delete(FileInfoEntity fileInfo) {
        try {
            check();
            amazonS3.deleteObject(bucketName, fileInfo.getSourceId());
            return true;
        } catch (AmazonServiceException e) {
            log.warn("Failed to delete the File!", e);
            return true;
        } catch (Exception e) {
            log.error("Failed to delete the File!", e);
            throw ExceptionUtils.mpe(DELETE_AMAZON_SERVICE_ERROR);
        }
    }

    @Override
    public DownloadModel download(FileInfoEntity fileInfo) {
        try {
            check();
            S3Object s3Object = amazonS3.getObject(bucketName, fileInfo.getSourceId());
            return DownloadModel.builder()
                    .filename(fileInfo.getFilename())
                    .contentType(s3Object.getObjectMetadata().getContentType())
                    .inputStream(s3Object.getObjectContent()).build();
        } catch (Exception e) {
            log.error("Failed to download the File!", e);
            throw ExceptionUtils.mpe(DOWNLOAD_AMAZON_SERVICE_ERROR);
        }
    }

    @Override
    public boolean update(FileInfoEntity fileInfo, MultipartFile file) {
        check();
        delete(fileInfo);
        store(fileInfo, file);
        return Boolean.TRUE;
    }

    @Override
    public void afterPropertiesSet() {
        amazonStorageSettingRepository.getFirstByOrderByModifyDateTime().ifPresent(this::createS3Client);
    }

    public void createS3Client(AmazonStorageSettingEntity amazonStorageSettingEntity) {
        if (amazonStorageSettingEntity == null || amazonStorageSettingEntity.isRemoved()) {
            if (amazonS3 == null) {
                return;
            }
            amazonS3.shutdown();
            amazonS3 = null;
            return;
        }
        bucketName = amazonStorageSettingEntity.getBucketName() + "/courier";
        String accessKeyId = AesUtil.decrypt(amazonStorageSettingEntity.getAccessKeyId());
        String accessKeyIdSecret = AesUtil.decrypt(amazonStorageSettingEntity.getAccessKeySecret());
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKeyId, accessKeyIdSecret);
        amazonS3 = AmazonS3Client.builder()
                .withRegion(amazonStorageSettingEntity.getRegion())
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    @Override
    public boolean isEnable() {
        return amazonS3 != null;
    }
}
