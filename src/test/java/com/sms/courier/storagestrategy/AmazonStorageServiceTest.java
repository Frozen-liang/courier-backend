package com.sms.courier.storagestrategy;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.sms.courier.entity.file.AmazonStorageSettingEntity;
import com.sms.courier.entity.file.FileInfoEntity;
import com.sms.courier.repository.AmazonStorageSettingRepository;
import com.sms.courier.storagestrategy.model.DownloadModel;
import com.sms.courier.storagestrategy.strategy.impl.AmazonStorageService;
import com.sms.courier.utils.AesUtil;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AmazonStorageServiceTest {

    private final AmazonStorageSettingRepository amazonStorageSettingRepository
            = mock(AmazonStorageSettingRepository.class);
    private final AmazonStorageService amazonStorageService = new AmazonStorageService(amazonStorageSettingRepository);
    private static final AmazonS3 amazonS3 = mock(AmazonS3.class);
    private final ObjectMetadata objectMetadata = mock(ObjectMetadata.class);
    private final MultipartFile multipartFile = mock(MultipartFile.class);
    private final PutObjectResult putObjectResult = mock(PutObjectResult.class);
    private final PutObjectRequest putObjectRequest = mock(PutObjectRequest.class);
    private final FileInfoEntity fileInfo = mock(FileInfoEntity.class);
    private final InputStream inputStream = mock(InputStream.class);
    private final DeleteObjectsRequest deleteObjectsRequest = mock(DeleteObjectsRequest.class);
    private final S3Object s3Object = mock(S3Object.class);
    private final DownloadModel downloadModel = mock(DownloadModel.class);
    private final S3ObjectInputStream s3ObjectInputStream = mock(S3ObjectInputStream.class);
    private final AWSStaticCredentialsProvider awsStaticCredentialsProvider = mock(AWSStaticCredentialsProvider.class);

    private static final String KEY = ObjectId.get().toString();
    private static final String ID = ObjectId.get().toString();
    private static final String NAME = ObjectId.get().toString();
    private static final String BUCKETNAMEPRE = ObjectId.get().toString();
    private static final String TYPE = ObjectId.get().toString();
    private static final String REGION = ObjectId.get().toString();


    @Test
    @DisplayName("Test store")
    public void store() throws IOException {
        doNothing().when(objectMetadata).setContentType(any());
//        doNothing().when(objectMetadata).setContentLength(any());
        when(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead)).thenReturn(putObjectRequest);
        when(multipartFile.getInputStream()).thenReturn(inputStream);
        when(amazonS3.putObject(putObjectRequest)).thenReturn(putObjectResult);
//        assertThat(amazonStorageService.store(fileInfo, multipartFile)).isTrue();
    }

    @Test
    @DisplayName("Test check")
    public void check() {
        // 私有方法
    }

    @Test
    @DisplayName("Test check")
    public void delete() {
//        doNothing().when(amazonS3.deleteObject(any()));
//        assertThat(amazonStorageService.delete(fileInfo)).isTrue();
    }

    @Test
    @DisplayName("Test download")
    public void download() {
        when(fileInfo.getSourceId()).thenReturn(ID);
        when(fileInfo.getFilename()).thenReturn(NAME);
        when(amazonS3.getObject(any())).thenReturn(s3Object);
        when(s3Object.getObjectMetadata()).thenReturn(objectMetadata);
        when(s3Object.getObjectContent()).thenReturn(s3ObjectInputStream);
//        assertThat(amazonStorageService.download(fileInfo)).isNotNull();
    }

    @Test
    @DisplayName("Test download")
    public void isEnable() {
//        assertThat(amazonStorageService.isEnable()).isTrue();
    }

    @Test
    @DisplayName("Test afterPropertiesSet")
    public void afterPropertiesSet() {
//        when(amazonStorageSettingRepository.getFirstByOrderByModifyDateTime().isPresent()).thenReturn(any());
        amazonStorageService.afterPropertiesSet();
    }

    @Test
    @DisplayName("Test afterPropertiesSet")
    public void createS3Client() {
        AmazonStorageSettingEntity storageSettingEntity = mock(AmazonStorageSettingEntity.class);
//        when(AesUtil.decrypt(storageSettingEntity.getAccessKeyId())).thenReturn(ID); // 空指针
//        when(AesUtil.decrypt(storageSettingEntity.getAccessKeySecret())).thenReturn(ID);
        BasicAWSCredentials basicAWSCredentials = mock(BasicAWSCredentials.class);
        when(storageSettingEntity.getRegion()).thenReturn(REGION);
//        when(new AWSStaticCredentialsProvider(basicAWSCredentials)).thenReturn(awsStaticCredentialsProvider);
//        amazonStorageService.createS3Client(storageSettingEntity);
    }
}
