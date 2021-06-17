package com.sms.satp.service;

import com.sms.satp.dto.request.TestFileRequest;
import com.sms.satp.dto.response.FileInfoResponse;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsResource;

public interface FileService {

    List<FileInfoResponse> list(ObjectId projectId);

    Boolean insertTestFile(TestFileRequest testFileRequest);

    Boolean updateTestFile(TestFileRequest testFileRequest);

    Boolean deleteTestFileById(String id);

    GridFsResource downloadTestFile(String id);

    FileInfoResponse findById(String id);
}
