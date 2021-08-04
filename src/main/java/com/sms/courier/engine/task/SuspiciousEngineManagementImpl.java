package com.sms.courier.engine.task;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.stereotype.Component;

@Component
public class SuspiciousEngineManagementImpl implements SuspiciousEngineManagement {

    private final AtomicReference<Integer> currentIndex = new AtomicReference<>(0);
    private final Map<String, Integer> engineIndexMapping = new ConcurrentHashMap<>();
    private final Map<Integer, List<String>> suspiciousEngineQueue = new ConcurrentHashMap<>();

    @Override
    public Integer increaseIndex() {
        return currentIndex.getAndUpdate(currentIndex -> (currentIndex + 1) % 60);
    }

    public Integer getCurrentIndex() {
        return currentIndex.get();
    }

    public void add(String engineId) {
        engineIndexMapping.compute(engineId, (key, value) -> {
            if (Objects.nonNull(value)) {
                suspiciousEngineQueue.remove(value);
            }
            int currentIndex = this.currentIndex.get() == 0 ? 59 : this.currentIndex.get();
            int cursor = currentIndex - 1;
            suspiciousEngineQueue.compute(cursor, (index, queue) -> {
                queue = Objects.requireNonNullElse(queue, Lists.newArrayList());
                queue.add(engineId);
                return queue;
            });
            return cursor;
        });
    }

    public void remove(String engineId) {
        engineIndexMapping.compute(engineId, (key, value) -> {
            if (Objects.nonNull(value)) {
                suspiciousEngineQueue.get(value).remove(engineId);
            }

            // equals remove operation.
            return null;
        });
    }

    @Override
    public List<String> get(Integer cursor) {
        return suspiciousEngineQueue.remove(cursor);
    }


}
