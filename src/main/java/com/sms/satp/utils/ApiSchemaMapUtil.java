package com.sms.satp.utils;

import static com.sms.satp.utils.ApiSchemaUtil.splitKeyFromRef;

import com.sms.satp.parser.schema.ApiSchema;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class ApiSchemaMapUtil {

    private Map<String, ApiSchema> root;
    private final Map<String, Set<String>> graph;
    private boolean hasLine = false;

    public ApiSchemaMapUtil(Map<String, ApiSchema> apiSchemaMap) {
        this.root = apiSchemaMap;
        Set<String> stringSet = apiSchemaMap.keySet();
        graph = new HashMap<>((int) Math.ceil(stringSet.size() / 0.75) + 1);
        apiSchemaMap.keySet().iterator().forEachRemaining(s -> graph.put(s, new HashSet<>()));
        buildGraph();
    }

    public void removeRef() {
        Iterator<Entry<String, ApiSchema>> entryIterator = root.entrySet().iterator();
        String key;
        while (entryIterator.hasNext()) {
            Map.Entry<String, ApiSchema> entry = entryIterator.next();
            key = entry.getKey();
            entry.setValue(removeItemRef(entry.getValue(), key));
        }
    }

    private ApiSchema removeItemRef(ApiSchema apiSchema, String key) {
        if (StringUtils.isNoneBlank(apiSchema.getRef())) {
            String refKey = splitKeyFromRef(apiSchema.getRef());
            if (!isCycle(key, refKey)) {
                apiSchema = root.get(refKey);
            }
        } else {
            if (Objects.nonNull(apiSchema.getProperties())) {
                apiSchema.setProperties(removeMapRef(apiSchema.getProperties(), key));
            } else if (Objects.nonNull(apiSchema.getItem())) {
                apiSchema.setItem(removeItemRef(apiSchema.getItem(), key));
            }
        }
        return apiSchema;
    }

    private Map<String, ApiSchema> removeMapRef(Map<String, ApiSchema> apiSchemaMap, String key) {
        Iterator<Entry<String, ApiSchema>> entryIterator = apiSchemaMap.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, ApiSchema> entry = entryIterator.next();
            entry.setValue(removeItemRef(entry.getValue(), key));
        }
        return apiSchemaMap;
    }

    private void buildGraph() {
        Iterator<Entry<String, ApiSchema>> entryIterator = root.entrySet().iterator();
        String key;
        while (entryIterator.hasNext()) {
            Map.Entry<String, ApiSchema> entry = entryIterator.next();
            key = entry.getKey();
            resolveItem(entry.getValue(), key);
        }
    }

    private void resolveItem(ApiSchema apiSchema, String key) {
        if (StringUtils.isNoneBlank(apiSchema.getRef())) {
            String refKey = splitKeyFromRef(apiSchema.getRef());
            graph.get(key).add(refKey);
        } else {
            if (Objects.nonNull(apiSchema.getProperties())) {
                resolveMap(apiSchema.getProperties(), key);
            } else if (Objects.nonNull(apiSchema.getItem())) {
                resolveItem(apiSchema.getItem(), key);
            }
        }
    }

    private void resolveMap(Map<String, ApiSchema> apiSchemaMap, String key) {
        Iterator<Entry<String, ApiSchema>> entryIterator = apiSchemaMap.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, ApiSchema> entry = entryIterator.next();
            resolveItem(entry.getValue(), key);
        }
    }

    private boolean isCycle(String key, String ref) {
        hasLine = false;
        Set<String> isVisit = new HashSet<>();
        try {
            hasPath(ref, key, isVisit);
        } catch (RuntimeException e) {
            return hasLine;
        }
        return hasLine;
    }

    private void hasPath(String start, String end, Set<String> isVisit) {
        if (StringUtils.equals(start, end)) {
            hasLine = true;
            throw new RuntimeException();
        }
        if (!isVisit.contains(start)) {
            isVisit.add(start);
            Set<String> stringSet = graph.get(start);
            if (stringSet.contains(end)) {
                hasLine = true;
                throw new RuntimeException();
            } else {
                for (String str : stringSet) {
                    hasPath(str, end, isVisit);
                }
            }
        }
    }

}