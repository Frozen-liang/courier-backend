package com.sms.satp.parser;

import com.sms.satp.parser.common.DocumentDefinition;

public interface DocumentReader {

    public DocumentDefinition readLocation(String location, String projectId);

    public DocumentDefinition readContents(String content, String projectId);
}
