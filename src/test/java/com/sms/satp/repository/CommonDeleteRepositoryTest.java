package com.sms.satp.repository;

import static com.sms.satp.common.exception.ErrorCode.DELETE_GLOBAL_ENVIRONMENT_ERROR_BY_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.mongodb.client.result.UpdateResult;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.repository.impl.CommonDeleteRepositoryImpl;
import java.util.Collections;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

@DisplayName("Tests for CommonDeleteRepositoryTest")
class CommonDeleteRepositoryTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final CommonDeleteRepository commonDeleteRepository = new CommonDeleteRepositoryImpl(mongoTemplate);
    private static final List<String> ID_LIST = Collections.singletonList(ObjectId.get().toString());
    private static final String ID = ObjectId.get().toString();

    @Test
    @DisplayName("Test the deleteByIds method in the CommonDeleteRepository")
    void delete_by_ids_test() {
        when(mongoTemplate.updateMulti(any(Query.class), any(UpdateDefinition.class), any(Class.class))).thenReturn(
            UpdateResult.acknowledged(1, 1L, null));
        assertThat(commonDeleteRepository.deleteByIds(ID_LIST, ApiEntity.class)).isTrue();
    }

    @Test
    @DisplayName("Test the deleteById method in the CommonDeleteRepository")
    void delete_by_id_test() {
        when(mongoTemplate.updateFirst(any(Query.class), any(UpdateDefinition.class), any(Class.class))).thenReturn(
            UpdateResult.acknowledged(1, 1L, null));
        assertThat(commonDeleteRepository.deleteById(ID, ApiEntity.class)).isTrue();
    }

}
