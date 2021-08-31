package com.sms.courier.common.listener;

import com.sms.courier.common.field.CommonField;
import com.sms.courier.common.listener.event.AddCaseEvent;
import com.sms.courier.common.listener.event.DeleteCaseEvent;
import com.sms.courier.entity.api.ApiEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class ApiCaseCountListener {

    private final MongoTemplate mongoTemplate;

    public ApiCaseCountListener(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @EventListener
    public void doProcess(AddCaseEvent addCaseEvent) {
        List<String> apiIds = addCaseEvent.getApiIds();
        if (CollectionUtils.isNotEmpty(apiIds)) {
            Map<Integer, List<String>> apiMap = getApiMap(apiIds);
            for (Entry<Integer, List<String>> entry : apiMap.entrySet()) {
                updateApi(entry.getValue(), addCaseEvent.getCaseType().getName(), entry.getKey());
            }
        }
    }

    @EventListener
    public void doProcess(DeleteCaseEvent deleteCaseEvent) {
        List<String> apiIds = deleteCaseEvent.getApiIds();
        if (CollectionUtils.isNotEmpty(apiIds)) {
            Map<Integer, List<String>> apiMap = getApiMap(apiIds);
            for (Entry<Integer, List<String>> entry : apiMap.entrySet()) {
                updateApi(entry.getValue(), deleteCaseEvent.getCaseType().getName(), -entry.getKey());
            }
        }
    }

    private Map<Integer, List<String>> getApiMap(List<String> apiIds) {
        HashMap<Integer, List<String>> apiMap = new HashMap<>();
        Map<String, List<String>> map = apiIds.stream().collect(Collectors.groupingBy(Function.identity()));
        for (Entry<String, List<String>> entry : map.entrySet()) {
            apiMap.compute(entry.getValue().size(), (key, value) -> {
                List<String> ids = Objects.requireNonNullElse(value, new ArrayList<>());
                ids.add(entry.getKey());
                return ids;
            });
        }
        return apiMap;
    }

    private void updateApi(List<String> apiIds, String fieldName, int inc) {
        Query query = new Query();
        query.addCriteria(Criteria.where(CommonField.ID.getName()).in(apiIds));
        Update update = new Update();
        update.inc(fieldName, inc);
        mongoTemplate.upsert(query, update, ApiEntity.class);
    }

}
