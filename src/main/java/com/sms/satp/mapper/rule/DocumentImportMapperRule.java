package com.sms.satp.mapper.rule;

import static com.sms.satp.common.ErrorCode.DOCUMENT_TYPE_ERROR;
import static com.sms.satp.common.ErrorCode.SAVE_MODE_ERROR;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.dto.SaveMode;
import com.sms.satp.parser.common.DocumentType;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class DocumentImportMapperRule {

    public DocumentType asDocumentType(String type) {
        DocumentType documentType = DocumentType.resolve(type.toUpperCase(Locale.US));
        if (Objects.nonNull(documentType)) {
            return documentType;
        } else {
            throw new ApiTestPlatformException(DOCUMENT_TYPE_ERROR);
        }
    }

    public String asContent(MultipartFile multipartFile) throws IOException {
        if (Objects.nonNull(multipartFile)) {
            return IOUtils.toString(multipartFile.getInputStream(), StandardCharsets.UTF_8);
        } else {
            return null;
        }
    }

    public SaveMode asSaveMode(String mode) {
        SaveMode saveMode = SaveMode.resolve(mode.toUpperCase(Locale.US));
        if (Objects.nonNull(saveMode)) {
            return saveMode;
        } else {
            throw new ApiTestPlatformException(SAVE_MODE_ERROR);
        }
    }

}