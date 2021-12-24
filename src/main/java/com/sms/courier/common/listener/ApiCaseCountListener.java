package com.sms.courier.common.listener;

import com.sms.courier.common.field.ApiHistoryField;
import com.sms.courier.common.field.CommonField;
import com.sms.courier.common.listener.event.AddCaseEvent;
import com.sms.courier.common.listener.event.DeleteCaseEvent;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.api.ApiHistoryEntity;
import com.sms.courier.repository.ApiHistoryRepository;
import com.sms.courier.repository.ApiRepository;
import com.sms.courier.repository.CustomizedApiRepository;
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
    private final ApiRepository apiRepository;

    public ApiCaseCountListener(MongoTemplate mongoTemplate, ApiRepository apiRepository) {
        this.mongoTemplate = mongoTemplate;
        this.apiRepository = apiRepository;
    }

    @EventListener
    public void doProcess(AddCaseEvent addCaseEvent) {
        List<String> apiIds = addCaseEvent.getApiIds();
        if (CollectionUtils.isNotEmpty(apiIds)) {
            if (Objects.nonNull(addCaseEvent.getCount())) {
                updateApi(apiIds, addCaseEvent.getCaseType().getName(), addCaseEvent.getCount());
                updateApiHistory(apiIds, addCaseEvent.getCaseType().getName(), addCaseEvent.getCount());
            } else {
                Map<Integer, List<String>> apiMap = getApiMap(apiIds);
                for (Entry<Integer, List<String>> entry : apiMap.entrySet()) {
                    updateApi(entry.getValue(), addCaseEvent.getCaseType().getName(), entry.getKey());
                    updateApiHistory(entry.getValue(), addCaseEvent.getCaseType().getName(), entry.getKey());
                }
            }
        }
    }

    @EventListener
    public void doProcess(DeleteCaseEvent deleteCaseEvent) {
        List<String> apiIds = deleteCaseEvent.getApiIds();
        if (CollectionUtils.isNotEmpty(apiIds)) {
            if (Objects.nonNull(deleteCaseEvent.getCount())) {
                updateApi(apiIds, deleteCaseEvent.getCaseType().getName(), -deleteCaseEvent.getCount());
                updateApiHistory(apiIds, deleteCaseEvent.getCaseType().getName(), -deleteCaseEvent.getCount());
            } else {
                Map<Integer, List<String>> apiMap = getApiMap(apiIds);
                for (Entry<Integer, List<String>> entry : apiMap.entrySet()) {
                    updateApi(entry.getValue(), deleteCaseEvent.getCaseType().getName(), -entry.getKey());
                    updateApiHistory(entry.getValue(), deleteCaseEvent.getCaseType().getName(), -entry.getKey());
                }
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
        if (inc < 0) {
            query.addCriteria(Criteria.where(fieldName).gt(0));
        }
        query.addCriteria(Criteria.where(CommonField.ID.getName()).in(apiIds));
        Update update = new Update();
        update.inc(fieldName, inc);
        mongoTemplate.updateMulti(query, update, ApiEntity.class);
    }

    private void updateApiHistory(List<String> apiIds, String fieldName, int inc) {
        List<String> historyIds = apiRepository.findAllByIdIsIn(apiIds).map(ApiEntity::getHistoryId)
            .collect(Collectors.toList());
        Query query = new Query();
        if (inc < 0) {
            query.addCriteria(Criteria.where(ApiHistoryField.RECORD_.getName() + fieldName).gt(0));
        }
        query.addCriteria(Criteria.where(CommonField.ID.getName()).in(historyIds));
        Update update = new Update();
        update.inc(ApiHistoryField.RECORD_.getName() + fieldName, inc);
        mongoTemplate.updateMulti(query, update, ApiHistoryEntity.class);
    }

}
