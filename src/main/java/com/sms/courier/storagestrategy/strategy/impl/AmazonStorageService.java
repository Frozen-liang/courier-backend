package com.sms.courier.storagestrategy.strategy.impl;

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
            log.error(e.getMessage());
            throw e;
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error("Amazon store error!", e);
            throw ExceptionUtils.mpe("Store error!");
        }
        return Boolean.TRUE;
    }

    private void check() {
        if (amazonS3 == null) {
            throw ExceptionUtils.mpe("The amazonS3 not config!");
        }
    }

    @Override
    public Boolean delete(FileInfoEntity fileInfo) {
        try {
            check();
            amazonS3.deleteObject(bucketName, fileInfo.getSourceId());
            return true;
        } catch (AmazonServiceException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Amazon delete error!", e);
            throw ExceptionUtils.mpe("Delete error!");
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
        } catch (AmazonServiceException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Amazon download error!", e);
            throw ExceptionUtils.mpe("Download error!");
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
        bucketName = amazonStorageSettingEntity.getBucketName();
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
