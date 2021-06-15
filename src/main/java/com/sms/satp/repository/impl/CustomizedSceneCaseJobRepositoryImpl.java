package com.sms.satp.repository.impl;

import static com.sms.satp.common.field.CommonFiled.CREATE_DATE_TIME;

import com.sms.satp.common.field.CommonFiled;
import com.sms.satp.common.field.SceneCaseJobFiled;
import com.sms.satp.common.field.SceneFiled;
import com.sms.satp.dto.request.SceneCaseJobRequest;
import com.sms.satp.entity.job.SceneCaseJob;
import com.sms.satp.repository.CustomizedSceneCaseJobRepository;
import com.sms.satp.utils.PageDtoConverter;
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
    public Page<SceneCaseJob> page(SceneCaseJobRequest sceneCaseJobRequest) {
        PageDtoConverter.frontMapping(sceneCaseJobRequest);
        Document document = new Document();
        document.put(SceneCaseJobFiled.API_TEST_CASE.getFiled(), true);
        document.put(SceneCaseJobFiled.JOB_STATUS.getFiled(), true);
        document.put(SceneCaseJobFiled.CREATE_USER_NAME.getFiled(), true);
        document.put(CREATE_DATE_TIME.getFiled(), true);
        document.put(SceneCaseJobFiled.MESSAGE.getFiled(), true);
        BasicQuery query = new BasicQuery(new Document(), document);
        CommonFiled.CREATE_USER_ID.in(sceneCaseJobRequest.getUserIds()).ifPresent(query::addCriteria);
        SceneFiled.SCENE_CASE_ID.is(sceneCaseJobRequest.getSceneCaseId()).ifPresent(query::addCriteria);
        long total = mongoTemplate.count(query, SceneCaseJob.class);
        Sort sort = Sort.by(Direction.fromString(sceneCaseJobRequest.getOrder()), sceneCaseJobRequest.getSort());
        Pageable pageable = PageRequest
            .of(sceneCaseJobRequest.getPageNumber(), sceneCaseJobRequest.getPageSize(), sort);
        List<SceneCaseJob> sceneCaseJobList = mongoTemplate.find(query.with(pageable), SceneCaseJob.class);
        return new PageImpl<>(sceneCaseJobList, pageable, total);
    }

}
