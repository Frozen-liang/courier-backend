package com.sms.courier.service;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.response.CountStatisticsResponse;
import com.sms.courier.entity.statistics.ClientPortStatisticEntity;
import com.sms.courier.repository.ClientPortStatisticRepository;
import com.sms.courier.repository.CommonStatisticsRepository;
import com.sms.courier.service.impl.ClientPortStatisticServiceImpl;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Test cases for ClientPortStatisticServiceTest")
public class ClientPortStatisticServiceTest {

    private final ClientPortStatisticRepository clientPortStatisticRepository = mock(
        ClientPortStatisticRepository.class);
    private final CommonStatisticsRepository commonStatisticsRepository = mock(CommonStatisticsRepository.class);
    private final ClientPortStatisticService clientPortStatisticService =
        new ClientPortStatisticServiceImpl(clientPortStatisticRepository, commonStatisticsRepository);
    private final static String MOCK_IP = "123";
    private final static Integer MOCK_DAY = 7;

    @Test
    @DisplayName("Test the add method in the ClientPortStatistic")
    public void add_test() {
        ClientPortStatisticEntity entity = ClientPortStatisticEntity.builder().build();
        when(clientPortStatisticRepository.save(any())).thenReturn(entity);
        Boolean isSuccess = clientPortStatisticService.add(MOCK_IP, MOCK_IP);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("An exception occurred while add ClientPortStatistic")
    public void add_test_thrownException() {
        when(clientPortStatisticRepository.save(any())).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> clientPortStatisticService.add(MOCK_IP, MOCK_IP))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the groupDayCount method in the ClientPortStatistic")
    public void groupDayCount_test() {
        List<CountStatisticsResponse> responses = Lists.newArrayList();
        when(commonStatisticsRepository.getGroupDayCount(any(), any())).thenReturn(responses);
        List<CountStatisticsResponse> dto = clientPortStatisticService.groupDayCount(MOCK_DAY);
        assertThat(dto).isNotEmpty();
    }

    @Test
    @DisplayName("An exception occurred while groupDayCount ClientPortStatistic")
    public void groupDayCount_test_thrownException() {
        when(commonStatisticsRepository.getGroupDayCount(any(), any())).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> clientPortStatisticService.groupDayCount(MOCK_DAY))
            .isInstanceOf(ApiTestPlatformException.class);
    }

}
