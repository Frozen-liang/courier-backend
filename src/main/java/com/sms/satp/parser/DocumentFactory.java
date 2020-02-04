package com.sms.satp.parser;

import com.sms.satp.parser.common.DocumentType;
import com.sms.satp.parser.model.ApiDocument;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;

public interface DocumentFactory extends  ApplicationContextAware, InitializingBean {

    public ApiDocument create(String location, DocumentType documentType);
}
