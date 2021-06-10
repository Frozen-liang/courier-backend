package com.sms.satp.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.sms.satp.dto.request.LogPageRequest;
import com.sms.satp.dto.request.TestFileRequest;
import com.sms.satp.entity.log.LogEntity;
import com.sms.satp.repository.impl.CustomizedFileRepositoryImpl;
import com.sms.satp.repository.impl.CustomizedLogRepositoryImpl;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.gridfs.GridFsUpload;
import org.springframework.web.multipart.MultipartFile;

@DisplayName("Tests for CustomizedFileRepository")
class CustomizedFileRepositoryTest {

    private final GridFsTemplate gridFsTemplate = mock(GridFsTemplate.class);
    private final CustomizedFileRepository customizedFileRepository = new CustomizedFileRepositoryImpl(gridFsTemplate);
    private static final int TOTAL_ELEMENTS = 20;
    private final ObjectId projectId = ObjectId.get();
    private final ObjectId id = ObjectId.get();

    @Test
    @DisplayName("Test the list method in the CustomizedFileRepository")
    public void list_test() {
        ArrayList<GridFSFile> gridFSFiles = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            gridFSFiles.add(mock(GridFSFile.class));
        }
        GridFSFindIterable gridFSFindIterable = mock(GridFSFindIterable.class);
        when(gridFsTemplate.find(any())).thenReturn(gridFSFindIterable);
        when(gridFSFindIterable.into(any())).thenReturn(gridFSFiles);
        List<GridFSFile> list = customizedFileRepository.list(ObjectId.get());
        assertThat(list).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("Test the insertTestFile method in the CustomizedFileRepository")
    public void insertTestFile_test() throws IOException {
        TestFileRequest testFileRequest = TestFileRequest.builder().projectId(projectId).build();
        MultipartFile testFile = mock(MultipartFile.class);
        testFileRequest.setTestFile(testFile);
        when(testFile.getInputStream()).thenReturn(null);
        when(testFile.getOriginalFilename()).thenReturn("test.txt");
        when(testFile.getContentType()).thenReturn("application/octet-stream");
        when(gridFsTemplate.store(any(), anyString(), anyString(), any())).thenReturn(ObjectId.get());
        assertThat(customizedFileRepository.insertTestFile(testFileRequest)).isTrue();
    }

    @Test
    @DisplayName("Test the updateTestFile method in the CustomizedFileRepository")
    public void updateTestFile_test() throws IOException {
        TestFileRequest testFileRequest = TestFileRequest.builder().id(id).projectId(projectId).build();
        MultipartFile testFile = mock(MultipartFile.class);
        testFileRequest.setTestFile(testFile);
        GridFSFile gridFSFile = mock(GridFSFile.class);
        when(gridFsTemplate.findOne(any())).thenReturn(gridFSFile);
        when(testFile.getInputStream()).thenReturn(null);
        when(testFile.getOriginalFilename()).thenReturn("test.txt");
        when(testFile.getContentType()).thenReturn("application/octet-stream");
        when(gridFsTemplate.store(any())).thenReturn(ObjectId.get());
        assertThat(customizedFileRepository.insertTestFile(testFileRequest)).isTrue();
    }

}
