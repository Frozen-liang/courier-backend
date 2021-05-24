package com.sms.satp.repository.impl;

import com.sms.satp.common.field.CommonFiled;
import com.sms.satp.common.field.SceneFiled;
import com.sms.satp.dto.request.SearchSceneCaseRequest;
import com.sms.satp.entity.scenetest.SceneCase;
import com.sms.satp.repository.CustomizedSceneCaseRepository;
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
public class CustomizedSceneCaseRepositoryImpl implements CustomizedSceneCaseRepository {

    private final MongoTemplate mongoTemplate;

    public CustomizedSceneCaseRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<SceneCase> search(SearchSceneCaseRequest searchDto, String projectId) {
        PageDtoConverter.frontMapping(searchDto);
        Query query = new Query();
        CommonFiled.PROJECT_ID.is(projectId).ifPresent(query::addCriteria);
        SceneFiled.NAME.is(searchDto.getName()).ifPresent(query::addCriteria);
        SceneFiled.GROUP_ID.is(searchDto.getGroupId()).ifPresent(query::addCriteria);
        SceneFiled.TEST_STATUS.in(searchDto.getTestStatus()).ifPresent(query::addCriteria);
        SceneFiled.CASE_TAG.in(searchDto.getCaseTag()).ifPresent(query::addCriteria);
        SceneFiled.PRIORITY.in(searchDto.getPriority()).ifPresent(query::addCriteria);
        SceneFiled.CREATE_USER_NAME.in(searchDto.getCreateUserName()).ifPresent(query::addCriteria);
        long total = mongoTemplate.count(query, SceneCase.class);
        Sort sort = Sort.by(Direction.fromString(searchDto.getOrder()), searchDto.getSort());
        Pageable pageable = PageRequest.of(searchDto.getPageNumber(), searchDto.getPageSize(), sort);
        List<SceneCase> sceneCaseList = mongoTemplate.find(query.with(pageable), SceneCase.class);
        return new PageImpl<>(sceneCaseList, pageable, total);
    }

}
