package com.sms.satp.service.impl;

import com.sms.satp.dto.request.TestFileRequest;
import com.sms.satp.repository.CustomizedFileRepository;
import com.sms.satp.service.FileService;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private final CustomizedFileRepository customizedFileRepository;

    public FileServiceImpl(CustomizedFileRepository customizedFileRepository) {
        this.customizedFileRepository = customizedFileRepository;
    }

    @Override
    public Boolean insertTestFile(TestFileRequest testFileRequest) {
        try {
            return customizedFileRepository.insertTestFile(testFileRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean updateTestFile(TestFileRequest testFileRequest) {
        try {
            return customizedFileRepository.updateTestFile(testFileRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean deleteTestFileById(String id) {
        return null;
    }

    @Override
    public GridFsResource downloadTestFile(ObjectId id) {
        return customizedFileRepository.downloadTestFile(id);
    }
}
