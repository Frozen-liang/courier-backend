package com.sms.courier.parser;

import com.sms.courier.parser.common.DocumentDefinition;

public interface DocumentReader {

    DocumentDefinition read(String source);

}
