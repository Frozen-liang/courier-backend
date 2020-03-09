package com.sms.satp.parser;

public interface DocumentReader<T> {

    public T readLocation(String location);

    public T readContents(String content);
}
