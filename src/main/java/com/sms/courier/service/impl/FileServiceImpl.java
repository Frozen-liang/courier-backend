package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.TEST_FILE;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.exception.ErrorCode.DELETE_TEST_FILE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DOWNLOAD_TEST_FILE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_TEST_FILE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_TEST_FILE_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.UPLOAD_TEST_FILE_ERROR;
import static com.sms.courier.utils.Assert.notNull;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.TestFileRequest;
import com.sms.courier.dto.response.FileInfoResponse;
import com.sms.courier.mapper.FileMapper;
import com.sms.courier.repository.CustomizedFileRepository;
import com.sms.courier.service.FileService;
import com.sms.courier.utils.ExceptionUtils;
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
    @LogRecord(operationType = ADD, operationModule = TEST_FILE,
        template = "{{#testFileRequest.testFile.originalFilename}}")
    public String insertTestFile(TestFileRequest testFileRequest) {
        try {

            return customizedFileRepository.insertTestFile(testFileRequest);
        } catch (Exception e) {
            log.error("Failed to upload the TestFile!");
            throw ExceptionUtils.mpe(UPLOAD_TEST_FILE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = TEST_FILE,
        template = "{{#testFileRequest.testFile.originalFilename}}")
    public Boolean updateTestFile(TestFileRequest testFileRequest) {
        try {
            return customizedFileRepository.updateTestFile(testFileRequest);
        } catch (ApiTestPlatformException courierException) {
            log.error(courierException.getMessage());
            throw courierException;
        } catch (Exception e) {
            log.error("Failed to edit the TestFile");
            throw ExceptionUtils.mpe(EDIT_TEST_FILE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = TEST_FILE,
        template = "{{#result.filename}}",
        enhance = @Enhance(enable = true))
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
            GridFsResource gridFsResource = customizedFileRepository.downloadTestFile(id);
            notNull(gridFsResource, THE_TEST_FILE_NOT_EXIST_ERROR, id);
            return gridFsResource;
        } catch (ApiTestPlatformException courierException) {
            log.error(courierException.getMessage());
            throw courierException;
        } catch (Exception e) {
            log.error("Failed to download the TestFile by id:{}", id);
            throw ExceptionUtils.mpe(DOWNLOAD_TEST_FILE_ERROR);
        }
    }

    @Override
    public FileInfoResponse findById(String id) {
        return fileMapper.toFileInfoResponse(customizedFileRepository.findById(id));
    }
}
