package com.sms.courier.service;

import com.sms.courier.dto.request.TestFileRequest;
import com.sms.courier.dto.response.FileInfoResponse;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsResource;

public interface FileService {

    List<FileInfoResponse> list(ObjectId projectId);

    String insertTestFile(TestFileRequest testFileRequest);

    Boolean updateTestFile(TestFileRequest testFileRequest);

    Boolean deleteTestFileById(String id);

    GridFsResource downloadTestFile(String id);

    FileInfoResponse findById(String id);
}
