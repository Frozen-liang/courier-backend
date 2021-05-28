package com.sms.satp.repository;

import com.sms.satp.dto.request.TestFileRequest;
import java.io.IOException;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsResource;

public interface CustomizedFileRepository {

    Boolean insertTestFile(TestFileRequest testFileRequest) throws IOException;

    Boolean updateTestFile(TestFileRequest testFileRequest) throws IOException;

    Boolean deleteTestFileById(String id);

    GridFsResource downloadTestFile(ObjectId id);
}
