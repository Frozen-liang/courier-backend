package com.sms.satp.repository.impl;

import com.sms.satp.common.SearchFiled;
import com.sms.satp.entity.scenetest.SceneCase;
import com.sms.satp.entity.scenetest.SceneCaseSearchDto;
import com.sms.satp.repository.CustomizedSceneCaseRepository;
import com.sms.satp.utils.PageDtoConverter;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class CustomizedSceneCaseRepositoryImpl implements CustomizedSceneCaseRepository {

    private final MongoTemplate mongoTemplate;

    public CustomizedSceneCaseRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<SceneCase> search(SceneCaseSearchDto searchDto, String projectId) {
        PageDtoConverter.frontMapping(searchDto);
        Query query = new Query();
        query.addCriteria(Criteria.where(SearchFiled.PROJECT_ID.getFiledName()).is(projectId));
        if (StringUtils.isNotBlank(searchDto.getName())) {
            query.addCriteria(Criteria.where(SearchFiled.NAME.getFiledName()).is(searchDto.getName()));
        }
        if (StringUtils.isNotBlank(searchDto.getGroupId())) {
            query.addCriteria(Criteria.where(SearchFiled.GROUP_ID.getFiledName()).is(searchDto.getGroupId()));
        }
        if (!CollectionUtils.isEmpty(searchDto.getTestStatus())) {
            query.addCriteria(Criteria.where(SearchFiled.TEST_STATUS.getFiledName()).in(searchDto.getTestStatus()));
        }
        if (!CollectionUtils.isEmpty(searchDto.getCaseTag())) {
            query.addCriteria(Criteria.where(SearchFiled.CASE_TAG.getFiledName()).in(searchDto.getCaseTag()));
        }
        if (!CollectionUtils.isEmpty(searchDto.getPriority())) {
            query.addCriteria(Criteria.where(SearchFiled.PRIORITY.getFiledName()).in(searchDto.getPriority()));
        }
        if (!CollectionUtils.isEmpty(searchDto.getCreateUserName())) {
            query.addCriteria(
                Criteria.where(SearchFiled.CREATE_USER_NAME.getFiledName()).in(searchDto.getCreateUserName()));
        }
        long total = mongoTemplate.count(query, SceneCase.class);
        Sort sort = Sort.by(Direction.fromString(searchDto.getOrder()), searchDto.getSort());
        Pageable pageable = PageRequest.of(searchDto.getPageNumber(), searchDto.getPageSize(), sort);
        List<SceneCase> sceneCaseList = mongoTemplate.find(query.with(pageable), SceneCase.class);
        return new PageImpl<>(sceneCaseList, pageable, total);
    }

}
