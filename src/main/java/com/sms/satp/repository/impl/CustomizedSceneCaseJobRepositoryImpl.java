package com.sms.satp.repository.impl;

import com.sms.satp.common.field.CommonFiled;
import com.sms.satp.common.field.SceneFiled;
import com.sms.satp.dto.PageDto;
import com.sms.satp.entity.job.SceneCaseJob;
import com.sms.satp.repository.CustomizedSceneCaseJobRepository;
import com.sms.satp.utils.PageDtoConverter;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class CustomizedSceneCaseJobRepositoryImpl implements CustomizedSceneCaseJobRepository {

    private final MongoTemplate mongoTemplate;

    public CustomizedSceneCaseJobRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<SceneCaseJob> page(String sceneCaseId, List<String> userIds, PageDto pageDto) {
        PageDtoConverter.frontMapping(pageDto);
        Query query = new Query();
        CommonFiled.CREATE_USER_ID.in(userIds).ifPresent(query::addCriteria);
        SceneFiled.SCENE_CASE_ID.is(sceneCaseId).ifPresent(query::addCriteria);
        long total = mongoTemplate.count(query, SceneCaseJob.class);
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize(), sort);
        List<SceneCaseJob> sceneCaseJobList = mongoTemplate.find(query.with(pageable), SceneCaseJob.class);
        return new PageImpl<>(sceneCaseJobList, pageable, total);
    }

}
