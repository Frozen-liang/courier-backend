package com.sms.courier.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.dto.request.NotificationTemplateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for NotificationTemplateMapper")
public class NotificationTemplateMapperTest {

    private NotificationTemplateMapper mapper = new NotificationTemplateMapperImpl();

    @Test
    void toEntity_test() {
        String title = "title";
        NotificationTemplateRequest request = mock(NotificationTemplateRequest.class);
        when(request.getTitle()).thenReturn(title);
        assertThat(mapper.toEntity(request).getTitle()).isEqualTo(title);
    }

    @Test
    void null_toEntity_test() {
        assertThat(mapper.toEntity(null)).isNull();
    }

}