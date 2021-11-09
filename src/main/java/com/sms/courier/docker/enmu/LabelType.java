package com.sms.courier.docker.enmu;

import com.sms.courier.engine.model.EngineMemberEntity;
import com.sms.courier.entity.mock.MockSettingEntity;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum LabelType {
    ENGINE("engine", EngineMemberEntity.class),
    MOCK("mock", MockSettingEntity.class);

    private final String name;
    private final Class<?> entityClass;
    private static final Map<String, String> labelMap = Map.of("label", "courier");
    private static final Map<String, LabelType> LABEL_TYPE_MAP = Arrays.stream(values())
        .collect(Collectors.toMap(LabelType::getName,
            Function.identity()));

    LabelType(String name, Class<?> entityClass) {
        this.name = name;
        this.entityClass = entityClass;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public String getName() {
        return name;
    }

    public static Map<String, String> getLabels() {
        return labelMap;
    }

    public Map<String, String> createLabel() {
        Map<String, String> label = new HashMap<>();
        label.put("type", getName());
        label.putAll(labelMap);
        return label;
    }

    public static LabelType resolverByName(String name) {
        return LABEL_TYPE_MAP.get(name);
    }
}
