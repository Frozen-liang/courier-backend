package com.sms.courier.storagestrategy.strategy.impl;

import static com.sms.courier.common.exception.ErrorCode.DELETE_TEST_FILE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DOWNLOAD_TEST_FILE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_TEST_FILE_NOT_EXIST_ERROR;
import static com.sms.courier.utils.Assert.notNull;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.entity.file.FileInfoEntity;
import com.sms.courier.repository.CustomizedFileRepository;
import com.sms.courier.storagestrategy.StorageType;
import com.sms.courier.storagestrategy.model.DownloadModel;
import com.sms.courier.storagestrategy.strategy.FileStorageService;
import com.sms.courier.storagestrategy.strategy.StorageStrategy;
import com.sms.courier.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.web.multipart.MultipartFile;

@StorageStrategy(type = StorageType.MONGO)
@Slf4j
public class MongoStorageService implements FileStorageService {

    private final CustomizedFileRepository customizedFileRepository;

    public MongoStorageService(CustomizedFileRepository customizedFileRepository) {
        this.customizedFileRepository = customizedFileRepository;
    }

    @Override
    public boolean store(FileInfoEntity fileInfo, MultipartFile file) {
        fileInfo.setSourceId(customizedFileRepository.insertTestFile(file));
        return Boolean.TRUE;
    }

    @Override
    public Boolean delete(FileInfoEntity fileInfo) {
        try {
            return customizedFileRepository.deleteTestFileById(fileInfo.getSourceId());
        } catch (Exception e) {
            log.error("Failed to delete the TestFile by id:{}", fileInfo.getSourceId());
            throw ExceptionUtils.mpe(DELETE_TEST_FILE_BY_ID_ERROR);
        }
    }

    @Override
    public DownloadModel download(FileInfoEntity fileInfo) {
        try {
            GridFsResource gridFsResource = customizedFileRepository.downloadTestFile(fileInfo.getSourceId());
            notNull(gridFsResource, THE_TEST_FILE_NOT_EXIST_ERROR, fileInfo.getSourceId());
            return DownloadModel.builder()
                    .contentType(gridFsResource.getContentType())
                    .filename(gridFsResource.getFilename())
                    .inputStream(gridFsResource.getInputStream())
                    .build();
        } catch (ApiTestPlatformException courierException) {
            log.error(courierException.getMessage());
            throw courierException;
        } catch (Exception e) {
            log.error("Failed to download the TestFile by id:{}", fileInfo.getSourceId());
            throw ExceptionUtils.mpe(DOWNLOAD_TEST_FILE_ERROR);
        }
    }

    @Override
    public boolean update(FileInfoEntity fileInfo, MultipartFile multipartFile) {
        return customizedFileRepository.updateTestFile(fileInfo, multipartFile);
    }

    @Override
    public boolean isEnable() {
        return true;
    }
}
