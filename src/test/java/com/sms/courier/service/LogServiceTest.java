package com.sms.courier.service;

import static com.sms.courier.common.enums.OperationType.ADD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.dto.request.LogPageRequest;
import com.sms.courier.entity.log.LogEntity;
import com.sms.courier.mapper.LogMapper;
import com.sms.courier.repository.CustomizedLogRepository;
import com.sms.courier.repository.LogRepository;
import com.sms.courier.service.impl.LogServiceImpl;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;

@DisplayName("Tests for LogService")
public class LogServiceTest {

    private final LogRepository logRepository = mock(LogRepository.class);
    private final CustomizedLogRepository customizedLogRepository = mock(CustomizedLogRepository.class);
    private final LogMapper logMapper = mock(LogMapper.class);
    private final LogService logService = new LogServiceImpl(logRepository, customizedLogRepository, logMapper);
    private final LogEntity log = LogEntity.builder().operationType(ADD).build();
    private final LogPageRequest logPageRequest = new LogPageRequest();

    @Test
    @DisplayName("Test the add method in the LogService service")
    public void add_test() {
        when(logRepository.insert(log)).thenReturn(log);
        assertThat(logService.add(log)).isTrue();
    }

    @Test
    @DisplayName("Test the add method throw exception in the LogService service")
    public void add_test_throw_exception() {
        when(logRepository.insert(log)).thenThrow(new RuntimeException());
        assertThat(logService.add(log)).isFalse();
    }

    @Test
    @DisplayName("Test the page method in the LogService service")
    public void page_test() {
        when(customizedLogRepository.page(logPageRequest)).thenReturn(new PageImpl<>(Collections.singletonList(log)));
        assertThat(logService.page(logPageRequest)).isNotNull();
    }

    @Test
    @DisplayName("Test the page method throw exception in the LogService service")
    public void page_test_throw_exception() {
        when(customizedLogRepository.page(logPageRequest)).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> logService.page(logPageRequest)).isInstanceOf(RuntimeException.class);
    }

}
