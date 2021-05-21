package com.sms.satp.service;

import static com.sms.satp.common.enums.OperationType.ADD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.satp.dto.request.LogPageRequest;
import com.sms.satp.entity.log.LogEntity;
import com.sms.satp.mapper.LogMapper;
import com.sms.satp.repository.CustomizedLogRepository;
import com.sms.satp.repository.LogRepository;
import com.sms.satp.service.impl.LogServiceImpl;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
        when(logRepository.save(log)).thenReturn(log);
        assertThat(logService.add(log)).isTrue();
    }

    @Test
    @DisplayName("Test the page method in the LogService service")
    public void page_test() {
        when(logRepository.save(log)).thenReturn(log);
        when(customizedLogRepository.page(logPageRequest)).thenReturn(new PageImpl<>(Collections.singletonList(log)));
        assertThat(logService.page(logPageRequest)).isNotNull();
    }

}
