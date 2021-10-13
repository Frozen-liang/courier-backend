package com.sms.courier.repository.impl;

import static com.sms.courier.common.field.ApiField.API_MANAGER_ID;
import static com.sms.courier.common.field.ApiField.API_NAME;
import static com.sms.courier.common.field.ApiField.API_PATH;
import static com.sms.courier.common.field.ApiField.API_PROTOCOL;
import static com.sms.courier.common.field.ApiField.API_STATUS;
import static com.sms.courier.common.field.ApiField.GROUP_ID;
import static com.sms.courier.common.field.ApiField.REQUEST_METHOD;
import static com.sms.courier.common.field.ApiField.SCENE_CASE_COUNT;
import static com.sms.courier.common.field.ApiField.TAG_ID;
import static com.sms.courier.common.field.ApiTagField.GROUP_NAME;
import static com.sms.courier.common.field.ApiTagField.TAG_NAME;
import static com.sms.courier.common.field.CommonField.CREATE_USER_ID;
import static com.sms.courier.common.field.CommonField.ID;
import static com.sms.courier.common.field.CommonField.MODIFY_DATE_TIME;
import static com.sms.courier.common.field.CommonField.PROJECT_ID;
import static com.sms.courier.common.field.CommonField.REMOVE;
import static com.sms.courier.common.field.UserField.NICKNAME;
import static com.sms.courier.common.field.UserField.USERNAME;

import com.sms.courier.common.enums.CollectionName;
import com.sms.courier.dto.request.ApiPageRequest;
import com.sms.courier.dto.request.UpdateRequest;
import com.sms.courier.dto.response.ApiPageResponse;
import com.sms.courier.dto.response.ApiResponse;
import com.sms.courier.dto.response.UserInfoResponse;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.group.ApiGroupEntity;
import com.sms.courier.entity.mongo.LookupField;
import com.sms.courier.entity.mongo.LookupVo;
import com.sms.courier.entity.tag.ApiTagEntity;
import com.sms.courier.repository.ApiGroupRepository;
import com.sms.courier.repository.ApiTagRepository;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.CustomizedApiRepository;
import com.sms.courier.repository.UserRepository;
import com.sms.courier.utils.PageDtoConverter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedApiRepositoryImpl implements CustomizedApiRepository {

    private final MongoTemplate mongoTemplate;
    private final CommonRepository commonRepository;
    private final ApiGroupRepository apiGroupRepository;
    private final ApiTagRepository apiTagRepository;
    private final UserRepository userRepository;

    public CustomizedApiRepositoryImpl(MongoTemplate mongoTemplate, CommonRepository commonRepository,
        ApiGroupRepository apiGroupRepository, ApiTagRepository apiTagRepository,
        UserRepository userRepository) {
        this.mongoTemplate = mongoTemplate;
        this.commonRepository = commonRepository;
        this.apiGroupRepository = apiGroupRepository;
        this.apiTagRepository = apiTagRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<ApiResponse> findById(String id) {
        return commonRepository.findById(id, CollectionName.API.getName(), getLookupVo(), ApiResponse.class);
    }

    @Override
    public Page<ApiPageResponse> page(ApiPageRequest apiPageRequest) {
        PageDtoConverter.frontMapping(apiPageRequest);
        Pageable pageable = PageDtoConverter.createPageable(apiPageRequest);
        Query query = new Query();
        addCriteria(apiPageRequest, query);
        long count = mongoTemplate.count(query, ApiEntity.class);
        if (count == 0) {
            return Page.empty();
        }
        query.with(pageable);
        List<ApiPageResponse> records = mongoTemplate.find(query, ApiPageResponse.class, CollectionName.API.getName());
        List<String> tagIds = new ArrayList<>();
        List<String> userIds = new ArrayList<>();
        records.forEach(api -> {
            Optional.ofNullable(api.getTagId()).ifPresent(tagIds::addAll);
            Optional.ofNullable(api.getApiManagerId()).ifPresent(userIds::add);
        });
        Map<String, String> tagMap = getTagMap(tagIds);
        Map<String, String> userMap = getUserMap(userIds);
        records.forEach((response) -> {
            response.setApiManager(userMap.get(response.getApiManagerId()));
            response.setTagName(getTagName(tagMap, response.getTagId()));
        });
        return new PageImpl<ApiPageResponse>(records,
            PageRequest.of(apiPageRequest.getPageNumber(), apiPageRequest.getPageSize()), count);
    }

    private List<LookupVo> pageLookup() {
        List<LookupField> managerUserField = List.of(
            LookupField.builder().field(USERNAME).alias("apiManager").build()
        );
        List<LookupField> tagField = List.of(LookupField.builder().field(TAG_NAME).build());
        return List.of(LookupVo.builder().from(CollectionName.API_TAG).localField(TAG_ID).foreignField(ID)
                .queryFields(tagField)
                .as("apiTag").build(),
            LookupVo.builder().from(CollectionName.USER).localField(API_MANAGER_ID).foreignField(ID).as(
                "manager")
                .queryFields(managerUserField).build()
        );
    }

    private List<LookupVo> getLookupVo() {
        List<LookupField> createUserField = List.of(
            LookupField.builder().field(USERNAME).alias("createUsername").build(),
            LookupField.builder().field(NICKNAME).alias("createNickname").build()
        );
        List<LookupField> groupField = List.of(LookupField.builder().field(GROUP_NAME).alias("groupName").build());
        List<LookupVo> pageLookup = new ArrayList<>(pageLookup());
        pageLookup.add(LookupVo.builder().from(CollectionName.API_GROUP).localField(GROUP_ID).foreignField(ID)
            .queryFields(groupField)
            .as("apiGroup").build());
        pageLookup.add(LookupVo.builder().from(CollectionName.USER).localField(CREATE_USER_ID).foreignField(ID).as(
            "createUser")
            .queryFields(createUserField).build());
        return pageLookup;
    }

    @Override
    public Boolean deleteById(String id) {
        return commonRepository.deleteById(id, ApiEntity.class);
    }

    @Override
    public Boolean deleteByIds(List<String> ids) {
        return commonRepository.deleteByIds(ids, ApiEntity.class);
    }

    @Override
    public Boolean recover(List<String> ids) {
        return commonRepository.recover(ids, ApiEntity.class);
    }

    @Override
    public void deleteByGroupIds(List<String> groupIds) {
        Query query = new Query(Criteria.where(GROUP_ID.getName()).in(groupIds));
        Update update = Update.update(REMOVE.getName(), Boolean.TRUE);
        update.set(MODIFY_DATE_TIME.getName(), LocalDateTime.now());
        update.unset(GROUP_ID.getName());
        mongoTemplate.updateMulti(query, update, ApiEntity.class);
    }

    @Override
    public Boolean updateFieldByIds(List<String> ids, UpdateRequest<Object> updateRequest) {
        return commonRepository.updateFieldByIds(ids, updateRequest, ApiEntity.class);
    }

    @Override
    public Boolean update(String json) {
        Document document = Document.parse(json);
        Update update = Update.fromDocument(document, "caseCount", "sceneCaseCount");
        Object id = document.get("id");
        if (Objects.isNull(id)) {
            return false;
        }
        Query query = Query.query(Criteria.where(ID.getName()).is(id));
        return mongoTemplate.updateFirst(query, update, ApiEntity.class).getModifiedCount() == 1;
    }

    @Override
    public Long sceneCount(ObjectId projectId) {
        Query query = new Query();
        PROJECT_ID.is(projectId).ifPresent(query::addCriteria);
        REMOVE.is(Boolean.FALSE).ifPresent(query::addCriteria);
        SCENE_CASE_COUNT.gt(0).ifPresent(query::addCriteria);
        return mongoTemplate.count(query, "Api");
    }

    private void addCriteria(ApiPageRequest apiPageRequest, Query query) {
        REMOVE.is(apiPageRequest.isRemoved()).ifPresent(query::addCriteria);
        PROJECT_ID.is(apiPageRequest.getProjectId()).ifPresent(query::addCriteria);
        API_NAME.like(apiPageRequest.getApiName()).ifPresent(query::addCriteria);
        API_PATH.like(apiPageRequest.getApiPath()).ifPresent(query::addCriteria);
        API_PROTOCOL.in(apiPageRequest.getApiProtocol()).ifPresent(query::addCriteria);
        API_STATUS.in(apiPageRequest.getApiStatus()).ifPresent(query::addCriteria);
        GROUP_ID.in(getApiGroupId(apiPageRequest.getGroupId())).ifPresent(query::addCriteria);
        REQUEST_METHOD.in(apiPageRequest.getRequestMethod()).ifPresent(query::addCriteria);
        TAG_ID.in(apiPageRequest.getTagId()).ifPresent(query::addCriteria);
        API_MANAGER_ID.in(apiPageRequest.getApiManagerId()).ifPresent(query::addCriteria);
    }

    private List<ObjectId> getApiGroupId(ObjectId groupId) {
        return Optional.ofNullable(groupId).map(ObjectId::toString)
            .map(apiGroupRepository::findById)
            .map((Optional::get))
            .map(ApiGroupEntity::getRealGroupId)
            .map(apiGroupRepository::findAllByPathContains)
            .orElse(Stream.empty())
            .map(ApiGroupEntity::getId)
            .map(ObjectId::new)
            .collect(Collectors.toList());
    }

    private List<String> getTagName(Map<String, String> tagMap, List<String> tagId) {
        List<String> tagName = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(tagId)) {
            tagId.forEach(a -> tagName.add(tagMap.get(a)));
        }
        return tagName;
    }

    private Map<String, String> getUserMap(List<String> userIds) {
        return userRepository.findByIdIn(userIds).stream()
            .collect(Collectors.toMap(UserInfoResponse::getId, UserInfoResponse::getUsername));
    }

    private Map<String, String> getTagMap(List<String> tagIds) {
        return apiTagRepository.findAllByIdIn(tagIds)
            .collect(Collectors.toMap(ApiTagEntity::getId, ApiTagEntity::getTagName));
    }
}
