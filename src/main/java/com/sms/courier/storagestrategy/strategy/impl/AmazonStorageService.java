package com.sms.courier.storagestrategy.strategy.impl;

import static com.sms.courier.common.exception.ErrorCode.CONFIG_AMAZON_SERVICE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_AMAZON_SERVICE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DOWNLOAD_AMAZON_SERVICE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.STORE_AMAZON_SERVICE_ERROR;

import com.sms.courier.entity.file.AmazonStorageSettingEntity;
import com.sms.courier.entity.file.FileInfoEntity;
import com.sms.courier.repository.AmazonStorageSettingRepository;
import com.sms.courier.storagestrategy.StorageType;
import com.sms.courier.storagestrategy.model.DownloadModel;
import com.sms.courier.storagestrategy.strategy.FileStorageService;
import com.sms.courier.storagestrategy.strategy.StorageStrategy;
import com.sms.courier.utils.AesUtil;
import com.sms.courier.utils.ExceptionUtils;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@StorageStrategy(type = StorageType.AMAZON)
@Slf4j
public class AmazonStorageService implements FileStorageService, InitializingBean {

    private final AmazonStorageSettingRepository amazonStorageSettingRepository;
    private S3Client amazonS3;
    private String bucketName;

    public AmazonStorageService(AmazonStorageSettingRepository amazonStorageSettingRepository) {
        this.amazonStorageSettingRepository = amazonStorageSettingRepository;
    }

    @Override
    public boolean store(FileInfoEntity fileInfo, MultipartFile file) {
        check();
        String key = "courier" + File.separator + fileInfo.getId() + "-" + file.getOriginalFilename();
        try {
            amazonS3.putObject(PutObjectRequest.builder().bucket(bucketName).key(key).build(),
                RequestBody.fromInputStream(file.getInputStream(), fileInfo.getLength()));
            fileInfo.setSourceId(key);
        } catch (Exception e) {
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
            amazonS3.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(fileInfo.getSourceId()).build());
            return true;
        } catch (S3Exception e) {
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
            ResponseInputStream<GetObjectResponse> object =
                amazonS3.getObject(GetObjectRequest.builder()
                    .bucket(bucketName).key(fileInfo.getSourceId()).build());
            return DownloadModel.builder()
                .filename(fileInfo.getFilename())
                .contentType(object.response().contentType())
                .inputStream(object).build();
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
            amazonS3.close();
            amazonS3 = null;
            return;
        }
        bucketName = amazonStorageSettingEntity.getBucketName();
        String accessKeyId = AesUtil.decrypt(amazonStorageSettingEntity.getAccessKeyId());
        String accessKeyIdSecret = AesUtil.decrypt(amazonStorageSettingEntity.getAccessKeySecret());
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKeyId, accessKeyIdSecret);
        amazonS3 = S3Client.builder()
            .region(Region.of(amazonStorageSettingEntity.getRegion()))
            .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
            .build();
    }

    @Override
    public boolean isEnable() {
        return amazonS3 != null;
    }
}
