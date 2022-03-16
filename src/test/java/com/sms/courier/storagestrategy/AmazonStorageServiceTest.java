package com.sms.courier.storagestrategy;

import static com.sms.courier.common.exception.ErrorCode.CONFIG_AMAZON_SERVICE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_AMAZON_SERVICE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DOWNLOAD_AMAZON_SERVICE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.entity.file.AmazonStorageSettingEntity;
import com.sms.courier.entity.file.FileInfoEntity;
import com.sms.courier.repository.AmazonStorageSettingRepository;
import com.sms.courier.storagestrategy.model.DownloadModel;
import com.sms.courier.storagestrategy.strategy.impl.AmazonStorageService;
import com.sms.courier.utils.AesUtil;
import java.io.IOException;
import java.io.InputStream;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

public class AmazonStorageServiceTest {

    private static final S3Client amazonS3 = mock(S3Client.class);
    private final AmazonStorageSettingRepository amazonStorageSettingRepository
            = mock(AmazonStorageSettingRepository.class);
    private final AmazonStorageService amazonStorageService = new AmazonStorageService(amazonStorageSettingRepository);

    private final MultipartFile multipartFile = mock(MultipartFile.class);
    private final FileInfoEntity fileInfo = mock(FileInfoEntity.class);
    private final InputStream inputStream = mock(InputStream.class);
    private final AmazonStorageSettingEntity entity = mock(AmazonStorageSettingEntity.class);

    private static final String KEY = ObjectId.get().toString();
    private static final String STRING = ObjectId.get().toString();
    private static final String REGION = ObjectId.get().toString();
    private static final long LONG = 2L;
    private static final String BUCKETNAME = ObjectId.get().toString();

    private final static MockedStatic<S3Client> S_3_CLIENT_MOCKED_STATIC;
    private final static S3ClientBuilder S_3_CLIENT_BUILDER = mock(S3ClientBuilder.class);

    static {
        S_3_CLIENT_MOCKED_STATIC = mockStatic(S3Client.class);
        S_3_CLIENT_MOCKED_STATIC.when(S3Client::builder).thenReturn(S_3_CLIENT_BUILDER);
        when(S_3_CLIENT_BUILDER.region(any(Region.class))).thenReturn(S_3_CLIENT_BUILDER);
        when(S_3_CLIENT_BUILDER.credentialsProvider(any())).thenReturn(S_3_CLIENT_BUILDER);
        when(S_3_CLIENT_BUILDER.build()).thenReturn(amazonS3);
    }

    @AfterAll
    public static void close() {
        S_3_CLIENT_MOCKED_STATIC.close();
    }

    @Test
    @DisplayName("Test the store method in the AmazonStorageService")
    public void store() throws IOException {
        createS3Client();
        // mock
        RequestBody requestBody = mock(RequestBody.class);
        PutObjectResponse putObjectResponse = mock(PutObjectResponse.class);
        when(fileInfo.getId()).thenReturn(STRING);
        when(multipartFile.getOriginalFilename()).thenReturn(STRING);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(BUCKETNAME).key(KEY).build();
        when(multipartFile.getInputStream()).thenReturn(inputStream);
        when(fileInfo.getLength()).thenReturn(LONG);
        when(amazonS3.putObject(putObjectRequest, requestBody)).thenReturn(putObjectResponse);
        doNothing().when(fileInfo).setSourceId(KEY);
        // 执行
        boolean result = amazonStorageService.store(fileInfo, multipartFile);
        // 验证
        assertThat(result).isTrue();

    }

    @Test
    @DisplayName("An exception occurred while upload TestFile")
    public void store_AmazonServiceException_test() {
        assertThatThrownBy(() -> amazonStorageService.store(fileInfo, multipartFile)).isInstanceOf(
                ApiTestPlatformException.class).extracting("code").isEqualTo(CONFIG_AMAZON_SERVICE_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the AmazonStorageService")
    public void delete() {
        createS3Client();
        // mock
        DeleteObjectResponse deleteObjectResponse = mock(DeleteObjectResponse.class);
        when(fileInfo.getSourceId()).thenReturn(STRING);
        when(amazonS3.deleteObject(any(DeleteObjectRequest.class))).thenReturn(deleteObjectResponse);
        // 执行
        Boolean delete = amazonStorageService.delete(fileInfo);
        // 验证
        assertThat(delete).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete TestFile")
    public void delete_exception_test() {
        createS3Client();
        // mock
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(BUCKETNAME).key(KEY).build();
        // 执行
        doThrow(new RuntimeException()).when(amazonS3).deleteObject(deleteObjectRequest);
        // 验证异常
        assertThatThrownBy(() -> amazonStorageService.delete(null)).isInstanceOf(
                ApiTestPlatformException.class).extracting("code").isEqualTo(DELETE_AMAZON_SERVICE_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the download method in the AmazonStorageService")
    public void download() {
        createS3Client();
        // mock
        ResponseInputStream<GetObjectResponse> inputStream = mock(ResponseInputStream.class);
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(BUCKETNAME).key(KEY).build();
        GetObjectResponse response = mock(GetObjectResponse.class);
        when(fileInfo.getSourceId()).thenReturn(STRING);
        when(amazonS3.getObject(any(GetObjectRequest.class))).thenReturn(inputStream);
        when(inputStream.response()).thenReturn(response);
        when(response.contentType()).thenReturn(STRING);
        when(fileInfo.getFilename()).thenReturn(STRING);
        // 执行
        DownloadModel download = amazonStorageService.download(fileInfo);
        // 验证
        assertThat(download).isNotNull();
    }

    @Test
    @DisplayName("An exception occurred while download TestFile")
    public void download_exception_test() {
        createS3Client();
        doThrow(new RuntimeException()).when(fileInfo).getFilename();
        assertThatThrownBy(() -> amazonStorageService.download(fileInfo)).isInstanceOf(
                ApiTestPlatformException.class).extracting("code").isEqualTo(DOWNLOAD_AMAZON_SERVICE_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the isEnable method in the AmazonStorageService")
    public void isEnable() {
        createS3Client();
        assertThat(amazonStorageService.isEnable()).isTrue();
    }

    @Test
    @DisplayName("Test the update method in the AmazonStorageService")
    public void update() throws IOException {
        createS3Client();
        RequestBody requestBody = mock(RequestBody.class);
        PutObjectResponse putObjectResponse = mock(PutObjectResponse.class);
        when(fileInfo.getId()).thenReturn(STRING);
        when(multipartFile.getOriginalFilename()).thenReturn(STRING);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(BUCKETNAME).key(KEY).build();
        when(multipartFile.getInputStream()).thenReturn(inputStream);
        when(fileInfo.getLength()).thenReturn(LONG);
        when(amazonS3.putObject(putObjectRequest, requestBody)).thenReturn(putObjectResponse);
        doNothing().when(fileInfo).setSourceId(KEY);
        assertThat(amazonStorageService.update(fileInfo, multipartFile)).isTrue();
    }

    public void createS3Client() {
        AmazonStorageSettingEntity amazonStorage =
                AmazonStorageSettingEntity.builder().bucketName(BUCKETNAME).accessKeyId(AesUtil.encrypt(
                        KEY)).region(REGION).accessKeySecret(AesUtil.encrypt(KEY)).build();
        amazonStorageService.createS3Client(amazonStorage);
    }
}
