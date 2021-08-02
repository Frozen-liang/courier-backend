package com.sms.satp.repository;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.sms.satp.dto.request.TestFileRequest;
import java.io.IOException;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsResource;

public interface CustomizedFileRepository {

    List<GridFSFile> list(ObjectId projectId);

    String insertTestFile(TestFileRequest testFileRequest) throws IOException;

    Boolean updateTestFile(TestFileRequest testFileRequest) throws IOException;

    Boolean deleteTestFileById(String id);

    GridFsResource downloadTestFile(String id);

    GridFSFile findById(String id);
}
