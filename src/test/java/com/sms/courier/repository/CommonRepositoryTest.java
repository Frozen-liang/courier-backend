package com.sms.courier.repository;

import static com.sms.courier.common.field.CommonField.CREATE_USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.mongodb.client.result.UpdateResult;
import com.sms.courier.common.enums.CollectionName;
import com.sms.courier.common.field.ApiField;
import com.sms.courier.common.field.Field;
import com.sms.courier.dto.PageDto;
import com.sms.courier.dto.request.UpdateRequest;
import com.sms.courier.dto.response.ApiResponse;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.mongo.LookupField;
import com.sms.courier.entity.mongo.LookupVo;
import com.sms.courier.entity.mongo.QueryVo;
import com.sms.courier.repository.impl.CommonRepositoryImpl;
import com.sms.courier.utils.SecurityUtil;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

@DisplayName("Tests for CommonRepositoryTest")
class CommonRepositoryTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final AggregationResults aggregationResults = mock(AggregationResults.class);
    private final Criteria criteria = mock(Criteria.class);

    private final Field field = ApiField.API_NAME;

    private final LookupVo lookupVo = LookupVo.builder().from(CollectionName.API).localField(field).foreignField(field)
        .as(COLLECTION_NAME)
        .queryFields(List.of(LookupField.builder().field(field).alias(COLLECTION_NAME).build())).build();
    private final QueryVo queryVo = QueryVo.builder().collectionName(COLLECTION_NAME).lookupVo(List.of(lookupVo))
        .criteriaList(List.of(Optional.of(criteria)))
        .build();

    private final CommonRepository commonRepository = new CommonRepositoryImpl(mongoTemplate);

    private static final List<String> ID_LIST = Collections.singletonList(ObjectId.get().toString());
    private static final String ID = ObjectId.get().toString();
    private static final String COLLECTION_NAME = "test";
    private static final MockedStatic<SecurityUtil> SECURITY_UTIL_MOCKED_STATIC;

    static {
        SECURITY_UTIL_MOCKED_STATIC = Mockito.mockStatic(SecurityUtil.class);
    }

    @AfterAll
    public static void close() {
        SECURITY_UTIL_MOCKED_STATIC.close();
    }

    @Test
    @DisplayName("Test the deleteByIds method in the CommonRepository")
    void delete_by_ids_test() {
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrUserId).thenReturn(ID);
        when(mongoTemplate.updateMulti(any(Query.class), any(UpdateDefinition.class), any(Class.class))).thenReturn(
            UpdateResult.acknowledged(1, 1L, null));
        assertThat(commonRepository.deleteByIds(ID_LIST, ApiEntity.class)).isTrue();
    }

    @Test
    @DisplayName("Test the deleteById method in the CommonRepository")
    void delete_by_id_test() {
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrUserId).thenReturn(ID);
        when(mongoTemplate.updateFirst(any(Query.class), any(UpdateDefinition.class), any(Class.class))).thenReturn(
            UpdateResult.acknowledged(1, 1L, null));
        assertThat(commonRepository.deleteById(ID, ApiEntity.class)).isTrue();
    }

    @Test
    @DisplayName("Test the removeTags method in the CommonRepository")
    public void remove_tags_test() {
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrUserId).thenReturn(ID);
        when(mongoTemplate.updateMulti(any(Query.class), any(UpdateDefinition.class), any(Class.class))).thenReturn(
            UpdateResult.acknowledged(1, 1L, null));
        assertThat(commonRepository.removeTags(field, ID_LIST, ApiEntity.class)).isTrue();
    }

    @Test
    @DisplayName("Test the recover method in the CommonRepository")
    public void recover_false_test() {
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrUserId).thenReturn(ID);
        assertThat(commonRepository.recover(null, ApiEntity.class)).isFalse();
    }

    @Test
    @DisplayName("Test the recover method in the CommonRepository")
    public void recover_true_test() {
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrUserId).thenReturn(ID);
        when(mongoTemplate.updateMulti(any(Query.class), any(UpdateDefinition.class), any(Class.class))).thenReturn(
            UpdateResult.acknowledged(1, 1L, null));
        assertThat(commonRepository.recover(ID_LIST, ApiEntity.class)).isTrue();
    }

    @Test
    @DisplayName("Test the findById method in the CommonRepository")
    public void findById_lookupVo_test() {
        when(mongoTemplate.aggregate(any(Aggregation.class), anyString(), any(Class.class)))
            .thenReturn(aggregationResults);
        when(aggregationResults.getUniqueMappedResult()).thenReturn(ApiResponse.builder().build());
        assertThat(commonRepository.findById(ID, COLLECTION_NAME, lookupVo, ApiResponse.class))
            .isNotEmpty();
    }

    @Test
    @DisplayName("Test the findById method in the CommonRepository")
    public void findById_lookupVo_list_test() {
        when(mongoTemplate.aggregate(any(Aggregation.class), anyString(), any(Class.class)))
            .thenReturn(aggregationResults);
        when(aggregationResults.getUniqueMappedResult()).thenReturn(ApiResponse.builder().build());
        assertThat(commonRepository.findById(ID, COLLECTION_NAME, List.of(lookupVo), ApiResponse.class))
            .isNotEmpty();
    }

    @Test
    @DisplayName("Test the list method in the CommonRepository")
    public void list1_test() {
        when(mongoTemplate.aggregate(any(Aggregation.class), anyString(), any(Class.class)))
            .thenReturn(aggregationResults);
        when(aggregationResults.getUniqueMappedResult()).thenReturn(ApiResponse.builder().build());
        assertThat(commonRepository.list(queryVo, ApiResponse.class)).isNotNull();
    }

    @Test
    @DisplayName("Test the list method in the CommonRepository")
    public void list2_test() {
        when(mongoTemplate.aggregate(any(Aggregation.class), anyString(), any(Class.class)))
            .thenReturn(aggregationResults);
        when(aggregationResults.getUniqueMappedResult()).thenReturn(ApiResponse.builder().build());
        assertThat(commonRepository.list(COLLECTION_NAME, lookupVo, List.of(Optional.of(criteria)), Class.class))
            .isNotNull();
    }

    @Test
    @DisplayName("Test the page method in the CommonRepository")
    public void page_empty_test() {
        when(mongoTemplate.count(any(Query.class), any(Class.class))).thenReturn(0L);
        assertThat(commonRepository.page(queryVo, PageDto.builder().build(), Class.class)).isEmpty();
    }

    @Test
    @DisplayName("Test the page method in the CommonRepository")
    public void page_not_empty_test() {
        when(mongoTemplate.count(any(Query.class), anyString())).thenReturn(1L);
        when(mongoTemplate.aggregate(any(Aggregation.class), anyString(), any(Class.class)))
            .thenReturn(aggregationResults);
        when(aggregationResults.getUniqueMappedResult()).thenReturn(ApiResponse.builder().build());
        assertThat(commonRepository.page(queryVo, PageDto.builder().build(), Class.class)).isNotNull();
    }

    @Test
    @DisplayName("Test the deleteFieldById method in the CommonRepository")
    void delete_field_by_ids_test() {
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrUserId).thenReturn(ID);
        when(mongoTemplate.updateMulti(any(Query.class), any(UpdateDefinition.class), any(Class.class))).thenReturn(
            UpdateResult.acknowledged(1, 1L, null));
        assertThat(commonRepository.deleteFieldByIds(ID_LIST, COLLECTION_NAME, ApiEntity.class)).isTrue();
    }

    @Test
    @DisplayName("Test the deleteFieldById method in the CommonRepository")
    void delete_field_by_id_test() {
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrUserId).thenReturn(ID);
        when(mongoTemplate.updateFirst(any(Query.class), any(UpdateDefinition.class), any(Class.class))).thenReturn(
            UpdateResult.acknowledged(1, 1L, null));
        assertThat(commonRepository.deleteFieldById(ID, COLLECTION_NAME, ApiEntity.class)).isTrue();
    }

    @Test
    @DisplayName("Test the updateFieldById method in the CommonRepository")
    void update_field_by_id_test() {
        Map<Field, Object> updateFields = new HashMap<>();
        updateFields.put(CREATE_USER_ID, ID);
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrUserId).thenReturn(ID);
        when(mongoTemplate.updateMulti(any(Query.class), any(UpdateDefinition.class), any(Class.class))).thenReturn(
            UpdateResult.acknowledged(1, 1L, null));
        assertThat(commonRepository.updateFieldById(ID, updateFields, ApiEntity.class)).isTrue();
    }

    @Test
    @DisplayName("Test the updateFieldByIds method in the CommonRepository")
    void update_field_by_ids_test() {
        Map<Field, Object> updateFields = new HashMap<>();
        updateFields.put(CREATE_USER_ID, ID);
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrUserId).thenReturn(ID);
        when(mongoTemplate.updateMulti(any(Query.class), any(UpdateDefinition.class), any(Class.class))).thenReturn(
            UpdateResult.acknowledged(1, 1L, null));
        assertThat(commonRepository.updateFieldByIds(List.of(ID), updateFields, ApiEntity.class)).isTrue();
    }

    @Test
    @DisplayName("Test the updateFieldByIds method in the CommonRepository")
    void update_field_by_ids2_test() {
        UpdateRequest<String> updateRequest = new UpdateRequest<>();
        updateRequest.setKey("age");
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrUserId).thenReturn(ID);
        when(mongoTemplate.updateMulti(any(Query.class), any(UpdateDefinition.class), any(Class.class))).thenReturn(
            UpdateResult.acknowledged(1, 1L, null));
        assertThat(commonRepository.updateFieldByIds(List.of(ID), updateRequest, ApiEntity.class)).isTrue();
    }

    @Test
    @DisplayName("Test the updateField method in the CommonRepository")
    void updateField_test() {
        when(mongoTemplate.updateMulti(any(Query.class), any(UpdateDefinition.class), any(Class.class))).thenReturn(
            UpdateResult.acknowledged(1, 1L, null));
        assertThat(commonRepository.updateField(new Query(), new Update(), ApiEntity.class)).isTrue();
    }
}
