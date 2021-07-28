package com.sms.satp.mapper;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.sms.satp.dto.response.FileInfoResponse;
import org.apache.commons.lang3.StringUtils;
import org.bson.BsonObjectId;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests for FileMapper")
public class FileMapperTest {

    private final FileMapper fileMapper = new FileMapperImpl();
    private static final Integer SIZE = 10;
    private static final String FILENAME = "test.txt";

    @Test
    @DisplayName("Test the method to convert the file object to a dto object")
    void dto_to_entity() {
        FileInfoResponse fileInfoResponse = fileMapper.toFileInfoResponse(createGridFsFile());
        assertThat(fileInfoResponse.getFilename()).isEqualTo(FILENAME);
    }

    @Test
    @DisplayName("Test the method for converting an file entity list object to a dto list object")
    void paramInfoList_to_paramInfoDtoList() {
        List<GridFSFile> fsFiles = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            fsFiles.add(createGridFsFile());
        }
        List<FileInfoResponse> fileInfoResponses = fileMapper.toFileInfoResponseList(fsFiles);
        assertThat(fileInfoResponses).hasSize(SIZE);
        assertThat(fileInfoResponses).allMatch(result -> StringUtils.equals(result.getFilename(), FILENAME));
    }

    private GridFSFile createGridFsFile() {
        Document metadata = new Document();
        metadata.put("projectId", ObjectId.get());
        metadata.put("uploadUser", "zhangsan");
        return new GridFSFile(new BsonObjectId(), FILENAME, 1000L, 1000, new Date(), metadata);
    }
}
