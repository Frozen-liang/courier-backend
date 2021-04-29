package com.sms.satp.parser;

import com.sms.satp.parser.common.DocumentParserResult;

public interface DocumentReader {

    public DocumentParserResult readLocation(String location);

    public DocumentParserResult readContents(String content);
}
