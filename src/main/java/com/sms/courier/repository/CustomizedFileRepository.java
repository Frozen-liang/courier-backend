package com.sms.courier.repository;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.sms.courier.entity.file.FileInfoEntity;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.web.multipart.MultipartFile;

public interface CustomizedFileRepository {

    List<GridFSFile> list(ObjectId projectId);

    String insertTestFile(MultipartFile multipartFile);

    Boolean updateTestFile(FileInfoEntity fileInfo, MultipartFile multipartFile);

    Boolean deleteTestFileById(String id);

    GridFsResource downloadTestFile(String id);

    GridFSFile findById(String id);
}
