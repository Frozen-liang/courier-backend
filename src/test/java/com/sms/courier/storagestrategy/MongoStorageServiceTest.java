package com.sms.courier.storagestrategy;

import com.sms.courier.entity.file.FileInfoEntity;
import com.sms.courier.repository.CustomizedFileRepository;
import com.sms.courier.repository.impl.CustomizedFileRepositoryImpl;
import com.sms.courier.storagestrategy.strategy.impl.MongoStorageService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("MongoStorageService Test")
public class MongoStorageServiceTest {
    private final GridFsTemplate gridFsTemplate = mock(GridFsTemplate.class);
    private final CustomizedFileRepository customizedFileRepository = new CustomizedFileRepositoryImpl(gridFsTemplate);
    private final MultipartFile multipartFile = mock(MultipartFile.class);
    private final FileInfoEntity fileInfo = FileInfoEntity.builder().id(ID).sourceId(ID).build();
    private final MongoStorageService mongoStorageService = mock(MongoStorageService.class);
    private final GridFsResource gridFsResource = mock(GridFsResource.class);
    private final InputStream inputStream = mock(InputStream.class);

    private static final String STRING = ObjectId.get().toString();
    private static final String ID = ObjectId.get().toString();

    @Test
    @DisplayName("Test store")
    public void store() {
//        when(customizedFileRepository.insertTestFile(multipartFile)).thenReturn(STRING);
//        when(fileInfo.setSourceId(any())).thenReturn();
    }

    @Test
    @DisplayName("Test store")
    public void delete() {
//        when(customizedFileRepository.deleteTestFileById(ID)).thenReturn(Boolean.TRUE);
//        assertThat(mongoStorageService.delete(fileInfo)).isTrue();
    }

    @Test
    @DisplayName("Test download")
    public void download() throws IOException {
//        when(fileInfo.getSourceId()).thenReturn(STRING);
//        when(customizedFileRepository.downloadTestFile(any())).thenReturn(gridFsResource);
        when(gridFsResource.getContentType()).thenReturn(STRING);
        when(gridFsResource.getFilename()).thenReturn(STRING);
        when(gridFsResource.getInputStream()).thenReturn(inputStream);
//        assertThat(mongoStorageService.download(fileInfo)).isNotNull();
    }
}
