package com.sms.satp.parser;

import com.sms.satp.parser.common.DocumentDefinition;

public interface DocumentReader {

    DocumentDefinition read(String source);

}
