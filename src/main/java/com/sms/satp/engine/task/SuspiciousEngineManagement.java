package com.sms.satp.engine.task;

public interface SuspiciousEngineManagement {

    Integer increaseIndex();

    Integer getCurrentIndex();

    void add(String engineId);

    void remove(String engineId);
}
