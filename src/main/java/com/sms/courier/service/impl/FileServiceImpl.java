package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.TEST_FILE;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.exception.ErrorCode.DELETE_TEST_FILE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DOWNLOAD_TEST_FILE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_TEST_FILE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.UPLOAD_TEST_FILE_ERROR;
import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.TestFileRequest;
import com.sms.courier.dto.response.FileInfoResponse;
import com.sms.courier.entity.file.FileInfoEntity;
import com.sms.courier.mapper.FileMapper;
import com.sms.courier.repository.FileInfoRepository;
import com.sms.courier.service.FileService;
import com.sms.courier.storagestrategy.StorageType;
import com.sms.courier.storagestrategy.model.DownloadModel;
import com.sms.courier.storagestrategy.strategy.FileStorageService;
import com.sms.courier.storagestrategy.strategy.StorageStrategyFactory;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.SecurityUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.RegionMetadata;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private final StorageStrategyFactory storageStrategyFactory;
    private final FileInfoRepository fileInfoRepository;
    private final FileMapper fileMapper;

    public FileServiceImpl(StorageStrategyFactory storageStrategyFactory, FileInfoRepository fileInfoRepository,
                           FileMapper fileMapper) {
        this.storageStrategyFactory = storageStrategyFactory;
        this.fileInfoRepository = fileInfoRepository;
        this.fileMapper = fileMapper;
    }

    @Override
    public Map<String, String> getAllRegion() {
        return Region.regions().stream().map(Region::metadata)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(RegionMetadata::id, RegionMetadata::description));
    }

    @Override
    public List<FileInfoResponse> list(String projectId) {
        return fileMapper.toFileInfoResponseList(fileInfoRepository.findByProjectIdIs(projectId));
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = TEST_FILE,
            template = "{{#testFileRequest.testFile.originalFilename}}")
    public String insertTestFile(TestFileRequest testFileRequest) {
        try {
            FileStorageService fileStorageService = storageStrategyFactory.fetchStorageStrategy();
            StorageType type = fileStorageService.getType();
            MultipartFile file = testFileRequest.getTestFile();
            FileInfoEntity fileInfo = FileInfoEntity.builder()
                    .id(ObjectId.get().toString())
                    .createDateTime(LocalDateTime.now())
                    .createUserId(SecurityUtil.getCurrUserId())
                    .filename(file.getOriginalFilename())
                    .length(file.getSize())
                    .projectId(testFileRequest.getProjectId())
                    .type(type)
                    .build();
            fileStorageService.store(fileInfo, file);
            fileInfoRepository.save(fileInfo);
            return fileInfo.getSourceId();
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
            FileInfoEntity fileInfo = getById(testFileRequest.getId());
            MultipartFile file = testFileRequest.getTestFile();
            FileStorageService fileStorageService = storageStrategyFactory.fetchStorageStrategy(fileInfo.getType());
            StorageType type = fileStorageService.getType();
            fileInfo.setType(type);
            fileInfo.setFilename(file.getOriginalFilename());
            fileInfo.setLength(file.getSize());
            fileStorageService.update(fileInfo, file);
            fileInfoRepository.save(fileInfo);
            return true;
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
            FileInfoEntity fileInfo = getById(id);
            storageStrategyFactory.fetchStorageStrategy(fileInfo.getType()).delete(fileInfo);
            fileInfoRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.error("Failed to edit the TestFile");
            throw ExceptionUtils.mpe(DELETE_TEST_FILE_BY_ID_ERROR);
        }

    }

    @Override
    public DownloadModel downloadTestFile(String id) {
        try {
            FileInfoEntity fileInfo = getById(id);
            return storageStrategyFactory.fetchStorageStrategy(fileInfo.getType()).download(fileInfo);
        } catch (Exception e) {
            log.error("Failed to download the TestFile");
            throw ExceptionUtils.mpe(DOWNLOAD_TEST_FILE_ERROR);
        }
    }

    @Override
    public FileInfoResponse findById(String id) {
        return fileInfoRepository.findById(id).map(fileMapper::toFileInfoResponse).orElse(new FileInfoResponse());
    }

    private FileInfoEntity getById(String id) {
        return fileInfoRepository
                .findById(id)
                .orElseThrow(() -> ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "FileInfo", id));
    }
}
