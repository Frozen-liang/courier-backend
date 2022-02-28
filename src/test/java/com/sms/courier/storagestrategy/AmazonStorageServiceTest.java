package com.sms.courier.storagestrategy;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

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
    private final DownloadModel downloadModel = mock(DownloadModel.class);
    private final S3ObjectInputStream s3ObjectInputStream = mock(S3ObjectInputStream.class);
    private final static AmazonS3ClientBuilder AMAZON_S3_BUILDER = mock(AmazonS3ClientBuilder.class);
    private final AWSStaticCredentialsProvider awsStaticCredentialsProvider = mock(AWSStaticCredentialsProvider.class);

    private final static MockedStatic<AmazonS3Client> AMAZON_MOCKED_STATIC;

    static {
        AMAZON_MOCKED_STATIC = mockStatic(AmazonS3Client.class);
        AMAZON_MOCKED_STATIC.when(AmazonS3Client::builder).thenReturn(AMAZON_S3_BUILDER);
        when(AMAZON_S3_BUILDER.withRegion(anyString())).thenReturn(AMAZON_S3_BUILDER);
        when(AMAZON_S3_BUILDER.withCredentials(any())).thenReturn(AMAZON_S3_BUILDER);
        when(AMAZON_S3_BUILDER.build()).thenReturn(amazonS3);
    }

    @AfterAll
    public static void close() {
        AMAZON_MOCKED_STATIC.close();
    }

    private static final String KEY = ObjectId.get().toString();
    private static final String ID = ObjectId.get().toString();
    private static final String NAME = ObjectId.get().toString();
    private static final String STRING = ObjectId.get().toString();
    private static final String TYPE = ObjectId.get().toString();
    private static final String REGION = ObjectId.get().toString();
    private static final long LONG = 2L;


    @Test
    @DisplayName("Test the store method in the AmazonStorageService")
    public void store() throws IOException {
        createS3Client();
        doNothing().when(objectMetadata).setContentType("application/octet-stream");
        doNothing().when(objectMetadata).setContentLength(LONG);
        when(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead)).thenReturn(putObjectRequest);
        when(multipartFile.getInputStream()).thenReturn(inputStream);
        when(amazonS3.putObject(putObjectRequest)).thenReturn(putObjectResult);
        doNothing().when(fileInfo).setSourceId(ID);
        assertThat(amazonStorageService.store(fileInfo, multipartFile)).isTrue();
    }

    @Test
    @DisplayName("Test the delete method in the AmazonStorageService")
    public void delete() {
        createS3Client();
        when(fileInfo.getProjectId()).thenReturn(STRING);
        doNothing().when(amazonS3).deleteObject(any(), any());
        assertThat(amazonStorageService.delete(fileInfo)).isTrue();
    }

    @Test
    @DisplayName("Test the download method in the AmazonStorageService")
    public void download() {
        createS3Client();
        S3Object s3Object = mock(S3Object.class);
        when(fileInfo.getSourceId()).thenReturn(ID);
        when(fileInfo.getFilename()).thenReturn(NAME);
        when(amazonS3.getObject(anyString(),anyString())).thenReturn(s3Object);
        when(s3Object.getObjectContent()).thenReturn(s3ObjectInputStream);
        when(s3Object.getObjectMetadata()).thenReturn(objectMetadata);
        assertThat(amazonStorageService.download(fileInfo)).isNotNull();
    }

    @Test
    @DisplayName("Test the isEnable method in the AmazonStorageService")
    public void isEnable() {
        createS3Client();
        assertThat(amazonStorageService.isEnable()).isTrue();
    }

    @Test
    @DisplayName("Test the update method in the AmazonStorageService")
    public void update() {
        createS3Client();
        assertThat(amazonStorageService.update(fileInfo,multipartFile)).isTrue();
    }

    public void createS3Client() {
        AmazonStorageSettingEntity amazonStorage = AmazonStorageSettingEntity.builder().accessKeyId(AesUtil.encrypt(
                KEY)).region(REGION).accessKeySecret(AesUtil.encrypt(KEY)).build();
        amazonStorageService.createS3Client(amazonStorage);
    }
}
