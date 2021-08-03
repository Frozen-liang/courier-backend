package com.sms.courier.repository.impl;

import static com.sms.courier.common.field.CommonField.CREATE_DATE_TIME;

import com.sms.courier.common.field.CommonField;
import com.sms.courier.common.field.SceneCaseJobField;
import com.sms.courier.common.field.SceneField;
import com.sms.courier.dto.request.SceneCaseJobRequest;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import com.sms.courier.repository.CustomizedSceneCaseJobRepository;
import com.sms.courier.utils.PageDtoConverter;
import java.util.List;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Component;

@Component
public class CustomizedSceneCaseJobRepositoryImpl implements CustomizedSceneCaseJobRepository {

    private final MongoTemplate mongoTemplate;

    public CustomizedSceneCaseJobRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<SceneCaseJobEntity> page(SceneCaseJobRequest sceneCaseJobRequest) {
        PageDtoConverter.frontMapping(sceneCaseJobRequest);
        Document document = new Document();
        document.put(SceneCaseJobField.API_TEST_CASE.getName(), true);
        document.put(SceneCaseJobField.JOB_STATUS.getName(), true);
        document.put(SceneCaseJobField.CREATE_USER_NAME.getName(), true);
        document.put(CREATE_DATE_TIME.getName(), true);
        document.put(SceneCaseJobField.MESSAGE.getName(), true);
        BasicQuery query = new BasicQuery(new Document(), document);
        CommonField.CREATE_USER_ID.in(sceneCaseJobRequest.getUserIds()).ifPresent(query::addCriteria);
        SceneField.SCENE_CASE_ID.is(sceneCaseJobRequest.getSceneCaseId()).ifPresent(query::addCriteria);
        SceneField.CASE_TEMPLATE_ID.is(sceneCaseJobRequest.getCaseTemplateId()).ifPresent(query::addCriteria);
        long total = mongoTemplate.count(query, SceneCaseJobEntity.class);
        Sort sort = Sort.by(Direction.fromString(sceneCaseJobRequest.getOrder()), sceneCaseJobRequest.getSort());
        Pageable pageable = PageRequest
            .of(sceneCaseJobRequest.getPageNumber(), sceneCaseJobRequest.getPageSize(), sort);
        List<SceneCaseJobEntity> sceneCaseJobList = mongoTemplate.find(query.with(pageable), SceneCaseJobEntity.class);
        return new PageImpl<>(sceneCaseJobList, pageable, total);
    }

}
