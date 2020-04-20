package com.sms.satp.entity.dto;

import static com.sms.satp.common.ErrorCode.IMPORT_FILE_FORMAT_ERROR;
import static com.sms.satp.common.ErrorCode.IMPORT_URL_FORMAT_ERROR;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.DocumentImport;
import com.sms.satp.parser.DocumentFactory;
import com.sms.satp.parser.model.ApiDocument;
import io.vavr.CheckedFunction2;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

@Slf4j
public enum ImportWay {
    FILE(ImportWay::parseFile),
    URL(ImportWay::parseUrl);

    private final CheckedFunction2<DocumentImport, DocumentFactory, ApiDocument> executor;

    ImportWay(CheckedFunction2<DocumentImport, DocumentFactory, ApiDocument> executor) {
        this.executor = executor;
    }

    public CheckedFunction2<DocumentImport, DocumentFactory, ApiDocument> getExecutor() {
        return executor;
    }

    private static final ApiDocument parseFile(
        DocumentImport documentImport, DocumentFactory documentFactory) {
        Assert.notNull(documentImport.getContent(), "File must not be empty!");
        try {
            return documentFactory.buildByContents(
                documentImport.getContent(), documentImport.getType());
        } catch (ApiTestPlatformException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to get the ApiDocument from file!", e);
            throw new ApiTestPlatformException(IMPORT_FILE_FORMAT_ERROR);
        }
    }

    private static final ApiDocument parseUrl(
        DocumentImport documentImport, DocumentFactory documentFactory) {
        Assert.notNull(documentImport.getUrl(), "URL must not be null!");
        try {
            return documentFactory.buildByResource(
                documentImport.getUrl(), documentImport.getType());
        } catch (ApiTestPlatformException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to get the ApiDocument from URL!", e);
            throw new ApiTestPlatformException(IMPORT_URL_FORMAT_ERROR);
        }
    }

    private static final Map<String, ImportWay> mappings = new HashMap<>(16);

    static {
        for (ImportWay importWay : values()) {
            mappings.put(importWay.name(), importWay);
        }
    }

    @Nullable
    public static ImportWay resolve(@Nullable String importWay) {
        return (importWay != null ? mappings.get(importWay) : null);
    }


    public boolean matches(String importWay) {
        return (this == resolve(importWay));
    }
}
