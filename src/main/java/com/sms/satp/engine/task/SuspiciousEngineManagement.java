package com.sms.satp.engine.task;

import java.util.List;

public interface SuspiciousEngineManagement {

    Integer increaseIndex();

    Integer getCurrentIndex();

    void add(String engineId);

    void remove(String engineId);

    List<String> get(Integer cursor);
}
