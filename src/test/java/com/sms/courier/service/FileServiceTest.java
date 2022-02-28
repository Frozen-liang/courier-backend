package com.sms.courier.service;

import static com.sms.courier.common.exception.ErrorCode.DELETE_TEST_FILE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DOWNLOAD_TEST_FILE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_TEST_FILE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.UPLOAD_TEST_FILE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.TestFileRequest;
import com.sms.courier.dto.response.FileInfoResponse;
import com.sms.courier.entity.file.FileInfoEntity;
import com.sms.courier.mapper.FileMapper;
import com.sms.courier.mapper.FileMapperImpl;
import com.sms.courier.repository.AmazonStorageSettingRepository;
import com.sms.courier.repository.CustomizedFileRepository;
import com.sms.courier.repository.FileInfoRepository;
import com.sms.courier.repository.impl.CustomizedFileRepositoryImpl;
import com.sms.courier.service.impl.FileServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.sms.courier.storagestrategy.StorageType;
import com.sms.courier.storagestrategy.model.DownloadModel;
import com.sms.courier.storagestrategy.strategy.FileStorageService;
import com.sms.courier.storagestrategy.strategy.StorageStrategyFactory;
import com.sms.courier.utils.SecurityUtil;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.multipart.MultipartFile;

@DisplayName("Test for FileService")
public class FileServiceTest {
    private final FileMapper fileMapper = new FileMapperImpl();
    private final GridFsTemplate gridFsTemplate = mock(GridFsTemplate.class);
    private final CustomizedFileRepository customizedFileRepository = new CustomizedFileRepositoryImpl(gridFsTemplate);
    private final FileInfoRepository fileInfoRepository = mock(FileInfoRepository.class);
    private final AmazonStorageSettingRepository AmazonStorageSettingRepository =
            mock(AmazonStorageSettingRepository.class);
    private final FileStorageService fileStorageService = mock(FileStorageService.class);
    private final StorageType storageType = mock(StorageType.class);
    private final StorageStrategyFactory storageStrategyFactory = mock(StorageStrategyFactory.class);
    private final DownloadModel downloadModel = mock(DownloadModel.class);
    private final FileService fileService = new FileServiceImpl(customizedFileRepository, storageStrategyFactory,
            fileInfoRepository, fileMapper);
    private final MultipartFile multipartFile = mock(MultipartFile.class);
    private static final String ID = ObjectId.get().toString();
    private static final Long SIZE = 1L;
    private static final String PROJECTID = ObjectId.get().toString();
    private static final String FILENAME = "FILENAME";
    private final FileInfoEntity fileInfo = FileInfoEntity.builder()
            .id(ID).projectId(ID).createDateTime(LocalDateTime.now()).createUserId(ID).build();
    private final TestFileRequest testFileRequest = mock(TestFileRequest.class);

    private static final MockedStatic<SecurityUtil> SECURITY_UTIL_MOCKED_STATIC;

    static {
        SECURITY_UTIL_MOCKED_STATIC = Mockito.mockStatic(SecurityUtil.class);
    }

    @AfterAll
    public static void close(){
        SECURITY_UTIL_MOCKED_STATIC.close();
    }

    @Test
    @DisplayName("Test the list method in the file service")
    public void list_test() {
        List<FileInfoEntity> fileInfoEntity = List.of(fileInfo);
        when(fileInfoRepository.findByProjectIdIs(PROJECTID)).thenReturn(fileInfoEntity);
        assertThat(fileService.list(PROJECTID)).hasSize(1);
    }

    @Test
    @DisplayName("Test the insertTestFile method in the file service")
    public void insertTestFile_test() {
        when(storageStrategyFactory.fetchStorageStrategy()).thenReturn(fileStorageService);
        when(fileStorageService.getType()).thenReturn(storageType);
        when(testFileRequest.getTestFile()).thenReturn(multipartFile);
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrUserId).thenReturn(ObjectId.get().toString());
        when(multipartFile.getSize()).thenReturn(SIZE);
        when(multipartFile.getOriginalFilename()).thenReturn(FILENAME);
        when(testFileRequest.getProjectId()).thenReturn(PROJECTID);
        when(fileStorageService.store(any(),any())).thenReturn(Boolean.TRUE);
        when(fileInfoRepository.save(any())).thenReturn(fileInfo);
        assertThat(fileService.insertTestFile(testFileRequest)).isNull();
    }

    @Test
    @DisplayName("An exception occurred while upload TestFile")
    public void insertTestFile_exception_test() {
        doThrow(new RuntimeException()).when(fileInfoRepository).save(fileInfo);
        assertThatThrownBy(() -> fileService.insertTestFile(testFileRequest)).isInstanceOf(
                ApiTestPlatformException.class).extracting("code").isEqualTo(UPLOAD_TEST_FILE_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the deleteTestFileById method in the file service")
    public void deleteTestFileById_test() {
        when(fileInfoRepository.deleteByIdIs(ID)).thenReturn(fileInfo);
        when(storageStrategyFactory.fetchStorageStrategy()).thenReturn(fileStorageService);
        when(fileStorageService.delete(fileInfo)).thenReturn(Boolean.TRUE);
        assertThat(fileService.deleteTestFileById(ID)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete TestFile")
    public void deleteTestFileById_exception_test() {
        when(fileInfoRepository.deleteByIdIs(ID)).thenThrow(new RuntimeException());
        when(storageStrategyFactory.fetchStorageStrategy()).thenReturn(fileStorageService);
        when(fileStorageService.delete(fileInfo)).thenReturn(Boolean.TRUE);
        assertThatThrownBy(() -> fileService.deleteTestFileById(ID)).isInstanceOf(
                ApiTestPlatformException.class).extracting("code").isEqualTo(DELETE_TEST_FILE_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the downloadTestFile method in the file service")
    public void downloadTestFile_test() {
        when(fileInfoRepository.findById(ID)).thenReturn(Optional.ofNullable(fileInfo));
        when(storageStrategyFactory.fetchStorageStrategy()).thenReturn(fileStorageService);
        when(fileStorageService.download(fileInfo)).thenReturn(downloadModel);
        assertThat(fileService.downloadTestFile(ID)).isNotNull();
    }

    @Test
    @DisplayName("An exception occurred while download TestFile")
    public void downloadTestFile_exception_test() {
        when(storageStrategyFactory.fetchStorageStrategy()).thenReturn(fileStorageService);
        when(fileStorageService.download(fileInfo)).thenReturn(downloadModel);
        when(fileInfoRepository.findById(ID)).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> fileService.downloadTestFile(ID)).isInstanceOf(
                ApiTestPlatformException.class).extracting("code").isEqualTo(DOWNLOAD_TEST_FILE_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the findById method in the file service")
    public void findById_test() {
        FileInfoResponse fileInfoResponse = fileService.findById(ID);
        assertThat(fileInfoResponse).isNotNull();
    }
}
