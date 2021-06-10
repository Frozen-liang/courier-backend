package com.sms.satp.service;

import static com.sms.satp.common.exception.ErrorCode.DOWNLOAD_TEST_FILE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_TEST_FILE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.UPLOAD_TEST_FILE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.TestFileRequest;
import com.sms.satp.mapper.FileMapper;
import com.sms.satp.mapper.FileMapperImpl;
import com.sms.satp.repository.CustomizedFileRepository;
import com.sms.satp.service.impl.FileServiceImpl;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import org.bson.BsonObjectId;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.gridfs.GridFsResource;

@DisplayName("Test for FileService")
public class FileServiceTest {

    private final CustomizedFileRepository customizedFileRepository =
        mock(CustomizedFileRepository.class);
    private final FileMapper fileMapper = new FileMapperImpl();
    private final FileService fileService = new FileServiceImpl(customizedFileRepository, fileMapper);
    private final ObjectId projectId = ObjectId.get();
    private TestFileRequest testFileRequest = TestFileRequest.builder().build();
    private static final String ID = ObjectId.get().toString();

    @Test
    @DisplayName("Test the list method in the file service")
    public void list_test() {
        when(customizedFileRepository.list(projectId)).thenReturn(Collections.singletonList(createGridFsFile()));
        assertThat(fileService.list(projectId)).hasSize(1);
    }

    @Test
    @DisplayName("Test the insertTestFile method in the file service")
    public void insertTestFile_test() throws IOException {
        when(customizedFileRepository.insertTestFile(testFileRequest)).thenReturn(Boolean.TRUE);
        assertThat(fileService.insertTestFile(testFileRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while upload TestFile")
    public void insertTestFile_exception_test() throws IOException {
        doThrow(new RuntimeException()).when(customizedFileRepository).insertTestFile(testFileRequest);
        assertThatThrownBy(() -> customizedFileRepository.insertTestFile(testFileRequest)).isInstanceOf(
            ApiTestPlatformException.class).extracting("code").isEqualTo(UPLOAD_TEST_FILE_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the updateTestFile method in the file service")
    public void updateTestFile_test() throws IOException {
        when(customizedFileRepository.updateTestFile(testFileRequest)).thenReturn(Boolean.TRUE);
        assertThat(fileService.updateTestFile(testFileRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while update TestFile")
    public void updateTestFile_exception_test() throws IOException {
        when(customizedFileRepository.updateTestFile(testFileRequest)).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> customizedFileRepository.updateTestFile(testFileRequest)).isInstanceOf(
            ApiTestPlatformException.class).extracting("code").isEqualTo(EDIT_TEST_FILE_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the downloadTestFile method in the file service")
    public void deleteTestFileById_test() {
        GridFsResource gridFsResource = mock(GridFsResource.class);
        when(customizedFileRepository.downloadTestFile(ID)).thenReturn(gridFsResource);
        assertThat(fileService.downloadTestFile(ID)).isNotNull();
    }

    @Test
    @DisplayName("An exception occurred while download TestFile")
    public void deleteTestFileById_exception_test() {
        when(customizedFileRepository.downloadTestFile(ID)).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> customizedFileRepository.downloadTestFile(ID)).isInstanceOf(
            ApiTestPlatformException.class).extracting("code").isEqualTo(DOWNLOAD_TEST_FILE_ERROR.getCode());
    }

    private GridFSFile createGridFsFile() {
        Document metadata = new Document();
        metadata.put("projectId", ObjectId.get());
        metadata.put("uploadUser", "zhangsan");
        return new GridFSFile(new BsonObjectId(), "test.txt", 1000L, 1000, new Date(), metadata);
    }
}
