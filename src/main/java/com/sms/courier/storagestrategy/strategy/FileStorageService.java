package com.sms.courier.storagestrategy.strategy;

import com.sms.courier.entity.file.FileInfoEntity;
import com.sms.courier.storagestrategy.StorageType;
import com.sms.courier.storagestrategy.model.DownloadModel;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    boolean store(FileInfoEntity fileInfo, MultipartFile file);

    Boolean delete(FileInfoEntity fileInfo);

    DownloadModel download(FileInfoEntity fileInfo);

    boolean update(FileInfoEntity fileInfo, MultipartFile file);

    boolean isEnable();

    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    default StorageType getType() {
        StorageStrategy storageStrategy = AnnotationUtils.findAnnotation(
                this.getClass(), StorageStrategy.class);
        return storageStrategy.type();
    }
}
