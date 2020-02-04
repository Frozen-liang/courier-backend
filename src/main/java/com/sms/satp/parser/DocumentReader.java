package com.sms.satp.parser;

public interface DocumentReader<T> {

    public T read(String location);
}
