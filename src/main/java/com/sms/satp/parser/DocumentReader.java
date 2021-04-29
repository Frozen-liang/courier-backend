package com.sms.satp.parser;

public interface DocumentReader<DocumentParserResult> {

    public DocumentParserResult readLocation(String location);

    public DocumentParserResult readContents(String content);
}
