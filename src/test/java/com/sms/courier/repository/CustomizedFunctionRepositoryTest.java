package com.sms.courier.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.dto.response.LoadFunctionResponse;
import com.sms.courier.entity.function.GlobalFunctionEntity;
import com.sms.courier.repository.impl.CustomizedFunctionRepositoryImpl;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

@DisplayName("Test for CustomizedFunctionRepository")
public class CustomizedFunctionRepositoryTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final CustomizedFunctionRepository customizedFunctionRepository =
        new CustomizedFunctionRepositoryImpl(mongoTemplate);
    private static final String PROJECT_ID = ObjectId.get().toString();
    private static final String WORKSPACE_ID = ObjectId.get().toString();

    @Test
    @DisplayName("Test loadFunction method in CustomizedFunctionRepository")
    public void loadFunction_test() {
        AggregationResults aggregationResults = mock(AggregationResults.class);
        when(aggregationResults.getMappedResults()).thenReturn(List.of(new LoadFunctionResponse()));
        when(mongoTemplate.aggregate(any(Aggregation.class), any(Class.class), any())).thenReturn(aggregationResults);
        List<LoadFunctionResponse> loadFunctionResponses = customizedFunctionRepository
            .loadFunction(PROJECT_ID, WORKSPACE_ID, GlobalFunctionEntity.class);
        assertThat(loadFunctionResponses).isNotEmpty();
    }
}
