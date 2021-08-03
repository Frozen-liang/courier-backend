package com.sms.courier.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.entity.datacollection.DataCollectionEntity;
import com.sms.courier.repository.impl.CustomizedDataCollectionRepositoryImpl;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

@DisplayName("Tests for CustomizedDataCollectionRepositoryTest")
class CustomizedDataCollectionRepositoryTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private CustomizedDataCollectionRepository customizedDataCollectionRepository =
        new CustomizedDataCollectionRepositoryImpl(mongoTemplate, commonRepository);
    private static final String ID = ObjectId.get().toString();
    private static final List<String> ID_LIST = Collections.singletonList(ID);

    @Test
    @DisplayName("Test the getParamList method in the CustomizedDataCollectionRepository")
    public void getParamList_test() {
        List<String> paramList = Arrays.asList("active", "city", "code");
        when(mongoTemplate.findOne(any(), any())).thenReturn(DataCollectionEntity.builder().paramList(paramList).build());
        List<String> result = customizedDataCollectionRepository.getParamListById(ID);
        assertThat(result).hasSize(paramList.size());
    }

    @Test
    @DisplayName("Test the deleteById method in the CustomizedDataCollectionRepository")
    public void deleteById_test() {
        when(commonRepository.deleteById(ID, DataCollectionEntity.class)).thenReturn(Boolean.TRUE);
        assertThat(customizedDataCollectionRepository.deleteById(ID)).isTrue();
    }

    @Test
    @DisplayName("Test the deleteByIds method in the CustomizedDataCollectionRepository")
    public void deleteByIds() {
        when(commonRepository.deleteByIds(ID_LIST, DataCollectionEntity.class)).thenReturn(Boolean.TRUE);
        assertThat(customizedDataCollectionRepository.deleteByIds(ID_LIST)).isTrue();
    }
}
