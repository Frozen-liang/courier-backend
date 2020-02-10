package com.sms.satp.parser.impl;

import static java.util.stream.Collectors.toMap;

import com.sms.satp.parser.DocumentFactory;
import com.sms.satp.parser.DocumentTransformer;
import com.sms.satp.parser.annotation.Reader;
import com.sms.satp.parser.common.DocumentType;
import com.sms.satp.parser.model.ApiDocument;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class DocumentFactoryImpl implements DocumentFactory {

    private ApplicationContext applicationContext = null;
    private Map<DocumentType, DocumentTransformer> transformerMap = new HashMap<>();


    @Override
    public void afterPropertiesSet() {
        Map<String, DocumentTransformer> transformers = this.applicationContext
            .getBeansOfType(DocumentTransformer.class);
        transformerMap = transformers.entrySet()
            .stream().parallel().collect(
                toMap(DocumentFactoryImpl::createReaderKey, Entry::getValue));
    }

    @Override
    public void setApplicationContext(@Nullable ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;
    }

    @Override
    public ApiDocument create(String location, DocumentType documentType) {
        DocumentTransformer<?> transformer = transformerMap.get(documentType);
        return transformer.build(location);
    }


    private static DocumentType createReaderKey(Entry<String, DocumentTransformer> entry) {
        Reader reader = entry.getValue().getClass()
            .getAnnotation(Reader.class);
        return reader.documentType();
    }
}
