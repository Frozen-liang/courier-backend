package com.sms.courier.repository;

import com.sms.courier.entity.file.FileInfoEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileInfoRepository extends MongoRepository<FileInfoEntity, String> {
    List<FileInfoEntity> findByProjectIdIs(String projectId);

    FileInfoEntity deleteByIdIs(String id);
}
