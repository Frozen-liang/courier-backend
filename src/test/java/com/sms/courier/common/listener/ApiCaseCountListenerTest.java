package com.sms.courier.common.listener;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.courier.common.enums.CaseType;
import com.sms.courier.common.listener.event.DeleteCaseEvent;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

@DisplayName("Test for ApiCaseCountListener")
public class ApiCaseCountListenerTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final ApiCaseCountListener apiCaseCountListener = new ApiCaseCountListener(mongoTemplate);

    @DisplayName("Test deleteCase in apiCaseCountListener")
    @Test
    public void deleteCase_test() {
        DeleteCaseEvent deleteCaseEvent = new DeleteCaseEvent(List.of("1", "1", "2", "1", "3", "4"), CaseType.CASE,
            null);
        when(mongoTemplate.updateMulti(any(), any(), any(Class.class))).thenReturn(null);
        apiCaseCountListener.doProcess(deleteCaseEvent);
        verify(mongoTemplate, times(2)).updateMulti(any(), any(), any(Class.class));
    }
}
