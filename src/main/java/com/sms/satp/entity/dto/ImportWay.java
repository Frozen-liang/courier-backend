package com.sms.satp.entity.dto;

import static com.sms.satp.common.ErrorCode.IMPORT_FILE_EMPTY_ERROR;
import static com.sms.satp.common.ErrorCode.IMPORT_URL_EMPTY_ERROE;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.DocumentImport;
import com.sms.satp.parser.DocumentFactory;
import com.sms.satp.parser.model.ApiDocument;
import io.vavr.CheckedFunction2;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

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
        if (StringUtils.isNoneBlank(documentImport.getContent())) {
            return documentFactory.buildByContents(
                documentImport.getContent(), documentImport.getType());
        } else {
            throw new ApiTestPlatformException(IMPORT_FILE_EMPTY_ERROR);
        }
    }

    private static final ApiDocument parseUrl(
        DocumentImport documentImport, DocumentFactory documentFactory) {
        if (StringUtils.isNoneBlank(documentImport.getUrl())) {
            return documentFactory.buildByResource(
                documentImport.getUrl(), documentImport.getType());
        } else {
            throw new ApiTestPlatformException(IMPORT_URL_EMPTY_ERROE);
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
