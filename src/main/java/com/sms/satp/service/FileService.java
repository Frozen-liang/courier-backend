package com.sms.satp.service;

import com.sms.satp.dto.request.TestFileRequest;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsResource;

public interface FileService {

    Boolean insertTestFile(TestFileRequest testFileRequest);

    Boolean updateTestFile(TestFileRequest testFileRequest);

    Boolean deleteTestFileById(String id);

    GridFsResource downloadTestFile(ObjectId id);
}
