package com.sms.satp.service.impl;

import static com.sms.satp.common.exception.ErrorCode.DELETE_TEST_FILE_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DOWNLOAD_TEST_FILE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_TEST_FILE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.UPLOAD_TEST_FILE_ERROR;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.TestFileRequest;
import com.sms.satp.dto.response.FileInfoResponse;
import com.sms.satp.mapper.FileMapper;
import com.sms.satp.repository.CustomizedFileRepository;
import com.sms.satp.service.FileService;
import com.sms.satp.utils.ExceptionUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private final CustomizedFileRepository customizedFileRepository;
    private final FileMapper fileMapper;

    public FileServiceImpl(CustomizedFileRepository customizedFileRepository, FileMapper fileMapper) {
        this.customizedFileRepository = customizedFileRepository;
        this.fileMapper = fileMapper;
    }

    @Override
    public List<FileInfoResponse> list(ObjectId projectId) {
        return fileMapper.toFileInfoResponseList(customizedFileRepository.list(projectId));
    }

    @Override
    public Boolean insertTestFile(TestFileRequest testFileRequest) {
        try {
            return customizedFileRepository.insertTestFile(testFileRequest);
        } catch (Exception e) {
            log.error("Failed to upload the TestFile!");
            throw ExceptionUtils.mpe(UPLOAD_TEST_FILE_ERROR);
        }
    }

    @Override
    public Boolean updateTestFile(TestFileRequest testFileRequest) {
        try {
            return customizedFileRepository.updateTestFile(testFileRequest);
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (Exception e) {
            log.error("Failed to edit the TestFile");
            throw ExceptionUtils.mpe(EDIT_TEST_FILE_ERROR);
        }
    }

    @Override
    public Boolean deleteTestFileById(String id) {
        try {
            return customizedFileRepository.deleteTestFileById(id);
        } catch (Exception e) {
            log.error("Failed to delete the TestFile by id:{}", id);
            throw ExceptionUtils.mpe(DELETE_TEST_FILE_BY_ID_ERROR);
        }
    }

    @Override
    public GridFsResource downloadTestFile(String id) {
        try {
            return customizedFileRepository.downloadTestFile(id);
        } catch (Exception e) {
            log.error("Failed to download the TestFile by id:{}", id.toString());
            throw ExceptionUtils.mpe(DOWNLOAD_TEST_FILE_ERROR);
        }
    }
}
