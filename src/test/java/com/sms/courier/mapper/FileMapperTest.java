package com.sms.courier.mapper;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.sms.courier.dto.response.FileInfoResponse;
import com.sms.courier.entity.file.FileInfoEntity;
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
    private final FileInfoEntity fileInfoEntity = new FileInfoEntity();
    private static final Integer SIZE = 10;
    private static final String FILENAME = "test.txt";
    private static final String ID = ObjectId.get().toString();
    private static final FileInfoEntity fileInfo = FileInfoEntity.builder().projectId(ID).build();
}
